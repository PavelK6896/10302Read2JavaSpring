package app.web.pavelk.read2.schema.type;

import app.web.pavelk.read2.schema.User;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomUserRoleListType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.ARRAY};
    }

    @Override
    public Class returnedClass() {
        return String[].class;
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        return false;
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return 0;
    }

    @Override
    public List<User.Role> nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        Array array = resultSet.getArray(strings[0]);
        if (array == null) {
            return Collections.emptyList();
        }
        String[] arr = (String[]) array.getArray();
        List<User.Role> roleList = new ArrayList<>();
        for (String s : arr) {
            roleList.add(User.Role.valueOf(s));
        }
        return roleList;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (o != null && sharedSessionContractImplementor != null) {
            List<User.Role> roleList = (List<User.Role>) o;
            String[] strings = roleList.stream().map(Enum::name).toArray(String[]::new);
            Array array = sharedSessionContractImplementor.connection().createArrayOf("text", strings);
            preparedStatement.setArray(i, array);
        } else {
            preparedStatement.setNull(i, sqlTypes()[0]);
        }
    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        return null;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        return null;
    }

    @Override
    public Object assemble(Serializable serializable, Object o) throws HibernateException {
        return null;
    }

    @Override
    public Object replace(Object o, Object o1, Object o2) throws HibernateException {
        return null;
    }
}