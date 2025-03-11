package io.factorialsystems.msscstore21authorization.security;

public class TenantContext {
    private static final ThreadLocal<String> tenantId = new ThreadLocal<>();

    public static String getTenantId() {
        return tenantId.get();
    }

    public static void setTenantId(String tenantId) {
        TenantContext.tenantId.set(tenantId);
    }

    public static void clear() {
        tenantId.remove();
    }
}