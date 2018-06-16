package ru.medisov.home_finance.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.medisov.home_finance.dao.model.CurrencyModel;
import ru.medisov.home_finance.dao.repository.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {
    private CurrencyModelDto model = getCurrencyModelDto();

    @Mock
    private Repository<CurrencyModel, Long> repositoryMock;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @BeforeEach
    void reset() {
        model = getCurrencyModelDto();
    }

    @Test
    @DisplayName("Search by name for an existing currency. Correct model returned")
    void findByNameIfExistsCorrectModelReturned() {
        CurrencyModel returnModel = new CurrencyModel().setId(1).setCode(model.getCode())
                                            .setName(model.getName()).setSymbol(model.getSymbol());
        when(repositoryMock.findByName(model.getName())).thenReturn(Optional.of(returnModel));

        assertEquals(Optional.of(new CurrencyModelDto(returnModel)), currencyService.findByName(model.getName()));
        verify(repositoryMock, times(1)).findByName(model.getName());
    }

    @Test
    @DisplayName("Attempt to search by name for a non-existent currency throws HomeFinanceServiceException")
    void findByNameIfNotExists() {
        when(repositoryMock.findByName(model.getName())).thenReturn(Optional.empty());

        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> currencyService
                .findByName(model.getName()));
        assertNotNull(thrown.getMessage());
    }

    @Test
    @DisplayName("Search for all currency models returns collection of models ")
    void findAllExistsOneEntry() {
        CurrencyModel returnModel = new CurrencyModel().setId(1).setCode(model.getCode())
                                        .setName(model.getName()).setSymbol(model.getSymbol());
        Collection<CurrencyModel> models = new ArrayList<>();
        models.add(returnModel);
        when(repositoryMock.findAll()).thenReturn(models);

        Collection<CurrencyModelDto> expected = models.stream().map(CurrencyModelDto::new).collect(Collectors.toList());
        Collection<CurrencyModelDto> actual = currencyService.findAll();
        assertEquals(expected, actual);
        assertTrue(actual.contains(new CurrencyModelDto(returnModel)));
        verify(repositoryMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Attempt to search currency models in empty table returns empty collection")
    void findAllEmptyTable() {
        Collection<CurrencyModel> emptyCollection = new ArrayList<>();
        when(repositoryMock.findAll()).thenReturn(emptyCollection);
        assertEquals(new ArrayList<CurrencyModelDto>(), currencyService.findAll());
    }

    @Test
    @DisplayName("Remove existing model returns true")
    void removeExistingEntryReturnsTrue() {
        when(repositoryMock.remove(model.getId())).thenReturn(true);
        assertTrue(currencyService.remove(model.getId()));
    }

    @Test
    @DisplayName("Remove non-existent model returns false")
    void removeIfNotExistsReturnsFalse() {
        when(repositoryMock.remove(model.getId())).thenReturn(false);
        assertFalse(currencyService.remove(model.getId()));
    }

    @Test
    @DisplayName("Save correct model. Successful Validation")
    void saveCorrectModelSuccessfulValidation() {
        CurrencyModel returnModel = new CurrencyModel().setId(1).setCode(model.getCode())
                                            .setName(model.getName()).setSymbol(model.getSymbol());

        when(repositoryMock.save(any())).thenReturn(returnModel);
        when(repositoryMock.findByName(model.getName())).thenReturn(Optional.empty());

        assertNotNull(currencyService);
        assertEquals(new CurrencyModelDto(returnModel), currencyService.save(model));

        verify(repositoryMock, times(1)).save(any());
        verify(repositoryMock, times(1)).findByName(returnModel.getName());
    }

    @Test
    @DisplayName("Attempt to save an incorrect model throws HomeFinanceServiceException. Validation not accepted")
    void saveIncorrectModelValidationNotAccepted() throws HomeFinanceServiceException {
        String emptyName = "";
        when(repositoryMock.findByName(emptyName)).thenReturn(Optional.empty());
        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> currencyService
                                                                        .save(model.setName(emptyName)));
        assertNotNull(thrown.getMessage());
        verify(repositoryMock, never()).save(any());
    }

    @Test
    @DisplayName("Attempt to save an incorrect model throws HomeFinanceServiceException. Varification not accepted")
    void saveExistingModelVerificationNotAccepted() throws HomeFinanceServiceException {
        String name = model.getName();
        CurrencyModel returnModel = new CurrencyModel().setName(name);

        when(repositoryMock.findByName(name)).thenReturn(Optional.of(returnModel));
        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> currencyService.save(model));
        assertNotNull(thrown.getMessage());
        verify(repositoryMock, never()).save(any());
    }

    @Test
    @DisplayName("update correct Model returns the same model")
    void updateCorrectModelSameModelReturned() {
        CurrencyModel returnModel = new CurrencyModel().setId(1).setCode(model.getCode())
                                            .setName(model.getName()).setSymbol(model.getSymbol());

        when(repositoryMock.update(any())).thenReturn(returnModel);
        assertEquals(new CurrencyModelDto(returnModel), currencyService.update(model));
        verify(repositoryMock, times(1)).update(any());
    }

    @Test
    @DisplayName("Attempt to update an incorrect Model throws HomeFinanceServiceException")
    void updateIncorrectModelCausesException() throws HomeFinanceServiceException {
        String emptyName = "";
        Throwable thrown = assertThrows(HomeFinanceServiceException.class, () -> currencyService.update(model.setName(emptyName)));
        assertNotNull(thrown.getMessage());
        verify(repositoryMock, never()).update(any());
    }

    private CurrencyModelDto getCurrencyModelDto() {
        return new CurrencyModelDto().setName("Боливиано").setCode("BOB").setSymbol("$");
    }
}