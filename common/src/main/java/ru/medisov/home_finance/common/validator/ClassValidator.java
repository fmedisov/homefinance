package ru.medisov.home_finance.common.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClassValidator implements Validator {

    public ClassValidator() {}

    @Override
    public boolean isValid(Object object) {
        try {
            Class oClass = object.getClass();
            Annotation annotationValid = oClass.getAnnotation(Valid.class);
            if (annotationValid == null) {
                throw new Exception("У класса " +  oClass.getSimpleName() + " нет аннотации Valid");
            }

            return isValidStrFields(object) & isCheckedForMin(object) & isCheckedDates(object) & isValidAmountFields(object);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean isCheckedDates(Object object) {
        Class oClass = object.getClass();
        List<Field> dataSinceList = getFieldsByAnnotation(oClass.getDeclaredFields(), DateSince.class);

        for (Field field : dataSinceList) {
            int annYear = field.getAnnotation(DateSince.class).year();
            int annMonth = field.getAnnotation(DateSince.class).month();
            int annDay = field.getAnnotation(DateSince.class).day();
            int annHour = field.getAnnotation(DateSince.class).hour();
            int annMinute = field.getAnnotation(DateSince.class).minute();

            try {
                LocalDateTime annDate = LocalDateTime.of(annYear, annMonth, annDay, annHour, annMinute);
                field.setAccessible(true);
                LocalDateTime fieldDate = (LocalDateTime) field.get(object);
                field.setAccessible(false);

                if (fieldDate.isBefore(annDate)) {
                    String message = "Дата в поле '" + field.getName() + "' должна быть после " + annDate;
                    throw new IncorrectDateException(message, new Throwable());
                }
            } catch (IncorrectDateException e) {
                System.out.println(e.getMessage());
                return false;
            } catch (IllegalAccessException e) {
                System.out.println("Нет доступа к полю " + field.getName());
                return false;
            }
        }
        return true;
    }

    private boolean isCheckedForMin(Object object) {
        Class oClass = object.getClass();
        List<Field> minValTestList = getFieldsByAnnotation(oClass.getDeclaredFields(), Min.class);

        for (Field field : minValTestList) {
            try {
                field.setAccessible(true);
                int annMinVal = field.getAnnotation(Min.class).minValue();
                int fieldVal = (int) field.get(object);
                field.setAccessible(false);
                if (annMinVal > fieldVal) {
                    String message = "Минимальное значение для поля '" + field.getName() + "' - " + annMinVal;
                    throw new MinValueException(message, new Throwable());
                }
            } catch (MinValueException e) {
                System.out.println(e.getMessage());
                return false;
            } catch (IllegalAccessException e) {
                System.out.println("Нет доступа к полю '" + field.getName() + "'");
                return false;
            }
        }
        return true;
    }

    private boolean isValidStrFields(Object object) {
        Class oClass = object.getClass();
        List<Field> notEmptyTestList = getFieldsByAnnotation(oClass.getDeclaredFields(), NotEmpty.class);

        for (Field field : notEmptyTestList) {
            try {
                field.setAccessible(true);
                String fieldVal = (String) field.get(object);
                field.setAccessible(false);

                if (fieldVal == null || fieldVal.length() == 0) {
                    String message = "Значение поля '" + field.getName();
                    message = message + "' не может быть " + (fieldVal == null ? "null" : "пустым");
                    throw new Exception(message, new Throwable());
                }
            } catch (IllegalAccessException e) {
                System.out.println("Нет доступа к полю '" + field.getName() + "'");
                return false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return true;
    }

    private boolean isValidAmountFields(Object object) {
        Class oClass = object.getClass();
        List<Field> notEmptyTestList = getFieldsByAnnotation(oClass.getDeclaredFields(), Amount.class);

        for (Field field : notEmptyTestList) {
            try {
                field.setAccessible(true);
                BigDecimal fieldVal = (BigDecimal) field.get(object);
                field.setAccessible(false);

                if (fieldVal == null) {
                    String message = "Значение поля '" + field.getName();
                    message = message + "' не может быть null";
                    throw new Exception(message, new Throwable());
                }
            } catch (IllegalAccessException e) {
                System.out.println("Нет доступа к полю '" + field.getName() + "'");
                return false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return true;
    }

    private List<Field> getFieldsByAnnotation(Field[] declaredFields, Class<? extends Annotation> aClass) {
        List<Field> fieldsList = new ArrayList<>();

        for (Field field : declaredFields) {
            Annotation annotation = field.getAnnotation(aClass);
            if (annotation != null) {
                fieldsList.add(field);
            }
        }

        return fieldsList;
    }
}