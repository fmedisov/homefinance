package ru.medisov.home_finance.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.medisov.home_finance.common.generator.TestModel;
import ru.medisov.home_finance.common.model.SimpleModel;
//import ru.medisov.home_finance.dao.repository.Repository;
import ru.medisov.home_finance.service.exception.HomeFinanceServiceException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommonServiceTest {

//    <T extends TagModel> void findByNameIfExistsCorrectModelReturned(Repository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
//        //arrange
//        T expectedModel = TestModel.generateModel(aModelClass);
//        expectedModel.setId(1L);
//        Optional<T> expected = Optional.of(expectedModel);
//        when(repositoryMock.findByName(expectedModel.getName())).thenReturn(expected);
//
//        //act
//        Optional<T> actual = service.findByName(expectedModel.getName());
//
//        //assert
//        assertEquals(expected, actual);
//        verify(repositoryMock, times(1)).findByName(expectedModel.getName());
//    }
//
//    <T extends TagModel> void findByNameIfNotExists(Repository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
//        T model = TestModel.generateModel(aModelClass);
//        model.setId(1L);
//        when(repositoryMock.findByName(model.getName())).thenReturn(Optional.empty());
//
//        assertThrows(HomeFinanceServiceException.class, () -> service.findByName(model.getName()));
//    }
//
//    <T extends TagModel> void findAllExistsOneEntry(Repository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
//        //arrange
//        T expectedModel = TestModel.generateModel(aModelClass);
//        expectedModel.setId(1L);
//        List<T> expected = new ArrayList<>();
//        expected.add(expectedModel);
//        when(repositoryMock.findAll()).thenReturn(expected);
//
//        //act
//        Collection<T> actual = service.findAll();
//
//        //assert
//        assertEquals(expected, actual);
//        assertTrue(actual.contains(expectedModel));
//        verify(repositoryMock, times(1)).findAll();
//    }
//
//    <T extends TagModel> void findAllEmptyTable(Repository<T, Long> repositoryMock, Service<T> service) {
//        List<T> emptyCollection = new ArrayList<>();
//        when(repositoryMock.findAll()).thenReturn(emptyCollection);
//
//        assertEquals(new ArrayList<T>(), service.findAll());
//    }
//
//    <T extends TagModel> void removeExistingEntryReturnsTrue(Repository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
//        T model = TestModel.generateModel(aModelClass);
//        model.setId(1L);
////        when(repositoryMock.remove(model.getId())).thenReturn(true);
//
//        assertTrue(service.remove(model.getId()));
//    }
//
//    <T extends TagModel> void removeIfNotExistsReturnsFalse(Repository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
//        T model = TestModel.generateModel(aModelClass);
//        model.setId(1L);
////        when(repositoryMock.remove(model.getId())).thenReturn(false);
//
//        assertFalse(service.remove(model.getId()));
//    }
//
//    <T extends TagModel> void saveCorrectModelSuccessfulValidation(Repository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
//        //arrange
//        T expected = TestModel.generateModel(aModelClass);
//        expected.setId(1L);
//        when(repositoryMock.save(any())).thenReturn(expected);
//
//        //act
//        T actual = service.save(expected);
//
//        //assert
//        assertEquals(expected, actual);
//        verify(repositoryMock, times(1)).save(any());
//    }
//
//    <T extends TagModel> void saveIncorrectModelValidationNotAccepted(Repository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service)
//                                                                            throws HomeFinanceServiceException {
//        T model = TestModel.generateModel(aModelClass);
//        String emptyName = "";
//        model.setName(emptyName);
//
//        assertThrows(HomeFinanceServiceException.class, () -> service.save(model));
//        verify(repositoryMock, never()).save(any());
//    }
//
//    <T extends TagModel> void updateCorrectModelSameModelReturned(Repository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
//        //arrange
//        T expected = TestModel.generateModel(aModelClass);
//        expected.setId(1L);
//        when(repositoryMock.update(any())).thenReturn(expected);
//
//        //act
//        T actual = service.update(expected);
//
//        //assert
//        assertEquals(expected, actual);
//        verify(repositoryMock, times(1)).update(any());
//    }
//
//    <T extends TagModel> void updateIncorrectModelCausesException(Repository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) throws HomeFinanceServiceException {
//        T model = TestModel.generateModel(aModelClass);
//        String emptyName = "";
//        model.setId(1L).setName(emptyName);
//
//        assertThrows(HomeFinanceServiceException.class, () -> service.update(model));
//        verify(repositoryMock, never()).update(any());
//    }

//    <T extends TagModel> void findByNameIfExistsCorrectModelReturned(JpaRepository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
//        //arrange
//        T expectedModel = TestModel.generateModel(aModelClass);
//        expectedModel.setId(1L);
//        Optional<T> expected = Optional.of(expectedModel);
//        when(repositoryMock.findByName(expectedModel.getName())).thenReturn(expected);
//
//        //act
//        Optional<T> actual = service.findByName(expectedModel.getName());
//
//        //assert
//        assertEquals(expected, actual);
//        verify(repositoryMock, times(1)).findByName(expectedModel.getName());
//    }
//
//    <T extends TagModel> void findByNameIfNotExists(JpaRepository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
//        T model = TestModel.generateModel(aModelClass);
//        model.setId(1L);
//        when(repositoryMock.findByName(model.getName())).thenReturn(Optional.empty());
//
//        assertThrows(HomeFinanceServiceException.class, () -> service.findByName(model.getName()));
//    }

    <T extends SimpleModel> void findAllExistsOneEntry(JpaRepository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
        //arrange
        T expectedModel = TestModel.generateModel(aModelClass);
        expectedModel.setId(1L);
        List<T> expected = new ArrayList<>();
        expected.add(expectedModel);
        when(repositoryMock.findAll()).thenReturn(expected);

        //act
        Collection<T> actual = service.findAll();

        //assert
        assertEquals(expected, actual);
        assertTrue(actual.contains(expectedModel));
        verify(repositoryMock, times(1)).findAll();
    }

    <T extends SimpleModel> void findAllEmptyTable(JpaRepository<T, Long> repositoryMock, Service<T> service) {
        List<T> emptyCollection = new ArrayList<>();
        when(repositoryMock.findAll()).thenReturn(emptyCollection);

        assertEquals(new ArrayList<T>(), service.findAll());
    }

    <T extends SimpleModel> void removeExistingEntryReturnsTrue(JpaRepository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
        T model = TestModel.generateModel(aModelClass);
        model.setId(1L);
//        when(repositoryMock.remove(model.getId())).thenReturn(true);

        assertTrue(service.remove(model.getId()));
    }

    <T extends SimpleModel> void removeIfNotExistsReturnsFalse(JpaRepository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
        T model = TestModel.generateModel(aModelClass);
        model.setId(1L);
//        when(repositoryMock.remove(model.getId())).thenReturn(false);

        assertFalse(service.remove(model.getId()));
    }

    <T extends SimpleModel> void saveCorrectModelSuccessfulValidation(JpaRepository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
        //arrange
        T expected = TestModel.generateModel(aModelClass);
        expected.setId(1L);
        when(repositoryMock.save(any())).thenReturn(expected);

        //act
        T actual = service.save(expected);

        //assert
        assertEquals(expected, actual);
        verify(repositoryMock, times(1)).save(any());
    }

    <T extends SimpleModel> void saveIncorrectModelValidationNotAccepted(JpaRepository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service)
            throws HomeFinanceServiceException {
        T model = TestModel.generateModel(aModelClass);
        String emptyName = "";
        model.setName(emptyName);

        assertThrows(HomeFinanceServiceException.class, () -> service.save(model));
        verify(repositoryMock, never()).save(any());
    }

    <T extends SimpleModel> void updateCorrectModelSameModelReturned(JpaRepository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) {
        //arrange
        T expected = TestModel.generateModel(aModelClass);
        expected.setId(1L);
        when(repositoryMock.saveAndFlush(any())).thenReturn(expected);

        //act
        T actual = service.update(expected);

        //assert
        assertEquals(expected, actual);
        verify(repositoryMock, times(1)).saveAndFlush(any());
    }

    <T extends SimpleModel> void updateIncorrectModelCausesException(JpaRepository<T, Long> repositoryMock, Class<T> aModelClass, Service<T> service) throws HomeFinanceServiceException {
        T model = TestModel.generateModel(aModelClass);
        String emptyName = "";
        T withId = (T) model.setId(1L);
        withId.setName(emptyName);

        assertThrows(HomeFinanceServiceException.class, () -> service.update(model));
        verify(repositoryMock, never()).saveAndFlush(any());
    }
}
