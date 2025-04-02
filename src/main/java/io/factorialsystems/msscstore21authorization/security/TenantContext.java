package io.factorialsystems.msscstore21authorization.security;

public class TenantContext {
    private static final ThreadLocal<String> tenantId = new ThreadLocal<>();
    private static final ThreadLocal<String> userId = new ThreadLocal<>();

    public static String getTenantId() {
        return tenantId.get();
    }
    public static void setTenantId(String tenantId) {
        TenantContext.tenantId.set(tenantId);
    }

    public static String getUserId() {return userId.get(); }
    public static void setUserId(String userId) {TenantContext.userId.set(userId); }
}