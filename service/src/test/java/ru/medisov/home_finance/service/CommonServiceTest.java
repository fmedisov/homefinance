package ru.medisov.home_finance.service;

import ru.medisov.home_finance.common.generator.TestModelGenerator;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.common.model.TagModel;
import ru.medisov.home_finance.dao.repository.ExtendedRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommonServiceTest {
    public <T extends TagModel> void findByNameIfExistsCorrectModelReturned(ExtendedRepository<T, Long> repositoryMock,
                                                                           TestModelGenerator generator, Class<T> aModelClass, Service<T> service) {
        //arrange
        T expectedModel = generator.generateModel(aModelClass);
        expectedModel.setId(1L);
        Optional<T> expected = Optional.of(expectedModel);
        when(repositoryMock.findByName(expectedModel.getName())).thenReturn(expected);

        //act
        Optional<T> actual = service.findByName(expectedModel.getName());

        //assert
        assertEquals(expected, actual);
        verify(repositoryMock, times(1)).findByName(expectedModel.getName());
    }

    public <T extends TagModel> void findByNameIfNotExists(ExtendedRepository<T, Long> repositoryMock,
                               TestModelGenerator generator, Class<T> aModelClass, Service<T> service) {
        T model = generator.generateModel(aModelClass);
        model.setId(1L);
        when(repositoryMock.findByName(model.getName())).thenReturn(Optional.empty());

        assertThrows(HomeFinanceServiceException.class, () -> service.findByName(model.getName()));
    }

    public <T extends TagModel> void findAllExistsOneEntry(ExtendedRepository<T, Long> repositoryMock,
                               TestModelGenerator generator, Class<T> aModelClass, Service<T> service) {
        //arrange
        T expectedModel = generator.generateModel(aModelClass);
        expectedModel.setId(1L);
        Collection<T> expected = new ArrayList<>();
        expected.add(expectedModel);
        when(repositoryMock.findAll()).thenReturn(expected);

        //act
        Collection<T> actual = service.findAll();

        //assert
        assertEquals(expected, actual);
        assertTrue(actual.contains(expectedModel));
        verify(repositoryMock, times(1)).findAll();
    }

    public <T extends TagModel> void findAllEmptyTable(ExtendedRepository<T, Long> repositoryMock, Service<T> service) {
        Collection<T> emptyCollection = new ArrayList<>();
        when(repositoryMock.findAll()).thenReturn(emptyCollection);

        assertEquals(new ArrayList<CurrencyModel>(), service.findAll());
    }

    public <T extends TagModel> void removeExistingEntryReturnsTrue(ExtendedRepository<T, Long> repositoryMock,
                                        TestModelGenerator generator, Class<T> aModelClass, Service<T> service) {
        T model = generator.generateModel(aModelClass);
        model.setId(1L);
        when(repositoryMock.remove(model.getId())).thenReturn(true);

        assertTrue(service.remove(model.getId()));
    }

    public <T extends TagModel> void removeIfNotExistsReturnsFalse(ExtendedRepository<T, Long> repositoryMock,
                                       TestModelGenerator generator, Class<T> aModelClass, Service<T> service) {
        T model = generator.generateModel(aModelClass);
        model.setId(1L);
        when(repositoryMock.remove(model.getId())).thenReturn(false);

        assertFalse(service.remove(model.getId()));
    }

    public <T extends TagModel> void saveCorrectModelSuccessfulValidation(ExtendedRepository<T, Long> repositoryMock,
                                              TestModelGenerator generator, Class<T> aModelClass, Service<T> service) {
        //arrange
        T expected = generator.generateModel(aModelClass);
        expected.setId(1L);
        when(repositoryMock.save(any())).thenReturn(expected);

        //act
        T actual = service.save(expected);

        //assert
        assertEquals(expected, actual);
        verify(repositoryMock, times(1)).save(any());
    }

    public <T extends TagModel> void saveIncorrectModelValidationNotAccepted(ExtendedRepository<T, Long> repositoryMock,
                                                            TestModelGenerator generator, Class<T> aModelClass, Service<T> service)
                                                                            throws HomeFinanceServiceException {
        T model = generator.generateModel(aModelClass);
        String emptyName = "";
        model.setName(emptyName);

        assertThrows(HomeFinanceServiceException.class, () -> service.save(model));
        verify(repositoryMock, never()).save(any());
    }

    public <T extends TagModel> void updateCorrectModelSameModelReturned(ExtendedRepository<T, Long> repositoryMock,
                                             TestModelGenerator generator, Class<T> aModelClass, Service<T> service) {
        //arrange
        T expected = generator.generateModel(aModelClass);
        expected.setId(1L);
        when(repositoryMock.update(any())).thenReturn(expected);

        //act
        T actual = service.update(expected);

        //assert
        assertEquals(expected, actual);
        verify(repositoryMock, times(1)).update(any());
    }

    public <T extends TagModel> void updateIncorrectModelCausesException(ExtendedRepository<T, Long> repositoryMock,
                                             TestModelGenerator generator, Class<T> aModelClass, Service<T> service) throws HomeFinanceServiceException {
        T model = generator.generateModel(aModelClass);
        String emptyName = "";
        model.setId(1L).setName(emptyName);

        assertThrows(HomeFinanceServiceException.class, () -> service.update(model));
        verify(repositoryMock, never()).update(any());
    }
}
