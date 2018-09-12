package ru.medisov.home_finance.web.config.social.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class CustomJdbcConnectionRepository implements ConnectionRepository {
    private final String userId;
    private final JdbcTemplate jdbcTemplate;
    private final ConnectionFactoryLocator connectionFactoryLocator;
    private final TextEncryptor textEncryptor;
    private final String tablePrefix;
    private final CustomJdbcConnectionRepository.ServiceProviderConnectionMapper connectionMapper = new CustomJdbcConnectionRepository.ServiceProviderConnectionMapper();

    public CustomJdbcConnectionRepository(String userId, JdbcTemplate jdbcTemplate, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor, String tablePrefix) {
        this.userId = userId;
        this.jdbcTemplate = jdbcTemplate;
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.textEncryptor = textEncryptor;
        this.tablePrefix = tablePrefix;
    }

    public MultiValueMap<String, Connection<?>> findAllConnections() {
        List<Connection<?>> resultList = this.jdbcTemplate.query(this.selectFromUserConnection() + " where userId = ? order by providerId, rate", this.connectionMapper, new Object[]{this.userId});
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap();
        Set<String> registeredProviderIds = this.connectionFactoryLocator.registeredProviderIds();
        Iterator var4 = registeredProviderIds.iterator();

        while(var4.hasNext()) {
            String registeredProviderId = (String)var4.next();
            connections.put(registeredProviderId, Collections.emptyList());
        }

        String providerId;
        Connection connection;
        for(var4 = resultList.iterator(); var4.hasNext(); connections.add(providerId, connection)) {
            connection = (Connection)var4.next();
            providerId = connection.getKey().getProviderId();
            if (((List)connections.get(providerId)).size() == 0) {
                connections.put(providerId, new LinkedList());
            }
        }

        return connections;
    }

    public List<Connection<?>> findConnections(String providerId) {
        return this.jdbcTemplate.query(this.selectFromUserConnection() + " where userId = ? and providerId = ? order by rate", this.connectionMapper, new Object[]{this.userId, providerId});
    }

    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        List<?> connections = this.findConnections(this.getProviderId(apiType));
        return (List<Connection<A>>) connections;
    }

    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
        if (providerUsers != null && !providerUsers.isEmpty()) {
            StringBuilder providerUsersCriteriaSql = new StringBuilder();
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userId", this.userId);
            Iterator it = providerUsers.entrySet().iterator();

            while(it.hasNext()) {
                Entry<String, List<String>> entry = (Entry)it.next();
                String providerId = (String)entry.getKey();
                providerUsersCriteriaSql.append("providerId = :providerId_").append(providerId).append(" and providerUserId in (:providerUserIds_").append(providerId).append(")");
                parameters.addValue("providerId_" + providerId, providerId);
                parameters.addValue("providerUserIds_" + providerId, entry.getValue());
                if (it.hasNext()) {
                    providerUsersCriteriaSql.append(" or ");
                }
            }

            List<Connection<?>> resultList = (new NamedParameterJdbcTemplate(this.jdbcTemplate)).query(this.selectFromUserConnection() + " where userId = :userId and " + providerUsersCriteriaSql + " order by providerId, rate", parameters, this.connectionMapper);
            MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap();
            Iterator var16 = resultList.iterator();

            while(var16.hasNext()) {
                Connection<?> connection = (Connection)var16.next();
                String providerId = connection.getKey().getProviderId();
                List<String> userIds = (List)providerUsers.get(providerId);
                List<Connection<?>> connections = (List)connectionsForUsers.get(providerId);
                if (connections == null) {
                    connections = new ArrayList(userIds.size());

                    for(int i = 0; i < userIds.size(); ++i) {
                        ((List)connections).add((Object)null);
                    }

                    connectionsForUsers.put(providerId, connections);
                }

                String providerUserId = connection.getKey().getProviderUserId();
                int connectionIndex = userIds.indexOf(providerUserId);
                ((List)connections).set(connectionIndex, connection);
            }

            return connectionsForUsers;
        } else {
            throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
        }
    }

    public Connection<?> getConnection(ConnectionKey connectionKey) {
        try {
            return (Connection)this.jdbcTemplate.queryForObject(this.selectFromUserConnection() + " where userId = ? and providerId = ? and providerUserId = ?", this.connectionMapper, new Object[]{this.userId, connectionKey.getProviderId(), connectionKey.getProviderUserId()});
        } catch (EmptyResultDataAccessException var3) {
            throw new NoSuchConnectionException(connectionKey);
        }
    }

    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        String providerId = this.getProviderId(apiType);
        return (Connection<A>) this.getConnection(new ConnectionKey(providerId, providerUserId));
    }

    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        String providerId = this.getProviderId(apiType);
        Connection<A> connection = (Connection<A>) this.findPrimaryConnection(providerId);
        if (connection == null) {
            throw new NotConnectedException(providerId);
        } else {
            return connection;
        }
    }

    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        String providerId = this.getProviderId(apiType);
        return (Connection<A>) this.findPrimaryConnection(providerId);
    }

    @Transactional
    public void addConnection(Connection<?> connection) {
        try {
            ConnectionData data = connection.createData();
            int rate = (Integer)this.jdbcTemplate.queryForObject("select coalesce(max(rate) + 1, 1) as rate from " + this.tablePrefix + "user_connection_tbl where userId = ? and providerId = ?", new Object[]{this.userId, data.getProviderId()}, Integer.class);
            this.jdbcTemplate.update("insert into " + this.tablePrefix + "user_connection_tbl (userId, providerId, providerUserId, rate, displayName, profileUrl, imageUrl, accessToken, secret, refreshToken, expireTime) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{this.userId, data.getProviderId(), data.getProviderUserId(), rate, data.getDisplayName(), data.getProfileUrl(), data.getImageUrl(), this.encrypt(data.getAccessToken()), this.encrypt(data.getSecret()), this.encrypt(data.getRefreshToken()), data.getExpireTime()});
        } catch (DuplicateKeyException var4) {
            throw new DuplicateConnectionException(connection.getKey());
        }
    }

    @Transactional
    public void updateConnection(Connection<?> connection) {
        ConnectionData data = connection.createData();
        this.jdbcTemplate.update("update " + this.tablePrefix + "user_connection_tbl set displayName = ?, profileUrl = ?, imageUrl = ?, accessToken = ?, secret = ?, refreshToken = ?, expireTime = ? where userId = ? and providerId = ? and providerUserId = ?", new Object[]{data.getDisplayName(), data.getProfileUrl(), data.getImageUrl(), this.encrypt(data.getAccessToken()), this.encrypt(data.getSecret()), this.encrypt(data.getRefreshToken()), data.getExpireTime(), this.userId, data.getProviderId(), data.getProviderUserId()});
    }

    @Transactional
    public void removeConnections(String providerId) {
        this.jdbcTemplate.update("delete from " + this.tablePrefix + "user_connection_tbl where userId = ? and providerId = ?", new Object[]{this.userId, providerId});
    }

    @Transactional
    public void removeConnection(ConnectionKey connectionKey) {
        this.jdbcTemplate.update("delete from " + this.tablePrefix + "user_connection_tbl where userId = ? and providerId = ? and providerUserId = ?", new Object[]{this.userId, connectionKey.getProviderId(), connectionKey.getProviderUserId()});
    }

    private String selectFromUserConnection() {
        return "select userId, providerId, providerUserId, displayName, profileUrl, imageUrl, accessToken, secret, refreshToken, expireTime from " + this.tablePrefix + "user_connection_tbl";
    }

    private Connection<?> findPrimaryConnection(String providerId) {
        List<Connection<?>> connections = this.jdbcTemplate.query(this.selectFromUserConnection() + " where userId = ? and providerId = ? order by rate", this.connectionMapper, new Object[]{this.userId, providerId});
        return connections.size() > 0 ? (Connection)connections.get(0) : null;
    }

    private <A> String getProviderId(Class<A> apiType) {
        return this.connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
    }

    private String encrypt(String text) {
        return text != null ? this.textEncryptor.encrypt(text) : text;
    }

    private final class ServiceProviderConnectionMapper implements RowMapper<Connection<?>> {
        private ServiceProviderConnectionMapper() {
        }

        public Connection<?> mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConnectionData connectionData = this.mapConnectionData(rs);
            ConnectionFactory<?> connectionFactory = CustomJdbcConnectionRepository.this.connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
            return connectionFactory.createConnection(connectionData);
        }

        private ConnectionData mapConnectionData(ResultSet rs) throws SQLException {
            return new ConnectionData(rs.getString("providerId"), rs.getString("providerUserId"), rs.getString("displayName"), rs.getString("profileUrl"), rs.getString("imageUrl"), this.decrypt(rs.getString("accessToken")), this.decrypt(rs.getString("secret")), this.decrypt(rs.getString("refreshToken")), this.expireTime(rs.getLong("expireTime")));
        }

        private String decrypt(String encryptedText) {
            return encryptedText != null ? CustomJdbcConnectionRepository.this.textEncryptor.decrypt(encryptedText) : encryptedText;
        }

        private Long expireTime(long expireTime) {
            return expireTime == 0L ? null : expireTime;
        }
    }
}
