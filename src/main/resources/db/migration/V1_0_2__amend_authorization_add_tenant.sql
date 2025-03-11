ALTER TABLE authorizations ADD COLUMN tenant_id VARCHAR(36);
ALTER TABLE authorizations ADD CONSTRAINT fk_authorizations_tenant_id FOREIGN KEY (tenant_id) REFERENCES tenants (id);