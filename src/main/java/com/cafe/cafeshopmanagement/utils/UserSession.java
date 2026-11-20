package com.cafe.cafeshopmanagement.utils;

public class UserSession {
    private static int userId = 1;
    private static String role = "";

    public static int getUserId() { return userId; }
    public static void setUserId(int id) { userId = id; }

    public static String getRole() { return role; }
    public static void setRole(String newRole) { role = newRole; }

    public static void clear() {
        userId = 1;
        role = "";
    }
}