package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        int sumCalories = 0;
        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        List<UserMealWithExceed> userMealWithExceedsPerDay = new ArrayList<>();
        LocalDateTime localDateTime = mealList.get(0).getDateTime();
        for (UserMeal meal : mealList) {
            if (localDateTime.toLocalDate() != meal.getDateTime().toLocalDate()) {
                if (sumCalories > caloriesPerDay) {
                    userMealWithExceeds.addAll(userMealWithExceedsPerDay);
                }
                userMealWithExceedsPerDay.clear();
                sumCalories = 0;
                localDateTime = meal.getDateTime();
            }
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                sumCalories += meal.getCalories();
                UserMealWithExceed userMealWithExceed = new UserMealWithExceed(meal.getDateTime(), "description", meal.getCalories(), true);
                userMealWithExceedsPerDay.add(userMealWithExceed);
            }
        }
        return userMealWithExceeds;
        // TODO return filtered list with correctly exceeded field
    }
}
