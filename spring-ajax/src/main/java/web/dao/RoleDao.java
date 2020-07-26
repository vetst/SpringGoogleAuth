package web.dao;

import web.model.Role;

public interface RoleDao {

    public void addRole(Role role);

    public Role getRoleByName(String name);
}
