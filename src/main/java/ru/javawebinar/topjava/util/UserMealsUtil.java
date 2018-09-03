package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = new HashMap<>();
        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        boolean isExceed;
        for (UserMeal meal : mealList) {
            LocalDate key = meal.getDateTime().toLocalDate();
            map.put(key, map.getOrDefault(key, 0) + meal.getCalories());
        }
        for (UserMeal meal : mealList) {
            LocalDate key = meal.getDateTime().toLocalDate();
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                int calories = map.getOrDefault(key, 0);
                isExceed = false;
                if (calories > caloriesPerDay) {
                    isExceed = true;
                }
                UserMealWithExceed userMealWithExceed = new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExceed);
                userMealWithExceeds.add(userMealWithExceed);
            }
        }

        //Optional Stream API
        Map<LocalDate, Integer> mapSummCaloriesPerDay = mealList.stream().collect(Collectors.groupingBy(p -> p.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        List<UserMealWithExceed> userMealWithExceeds2 = mealList.stream().filter(p -> TimeUtil.isBetween(p.getDateTime().toLocalTime(), startTime, endTime))
                .map(p -> new UserMealWithExceed(p.getDateTime(), p.getDescription(), p.getCalories(), mapSummCaloriesPerDay.get(p.getDateTime().toLocalDate()) > caloriesPerDay)).collect(toList());

        return userMealWithExceeds;
        // TODO return filtered list with correctly exceeded field
    }
}
