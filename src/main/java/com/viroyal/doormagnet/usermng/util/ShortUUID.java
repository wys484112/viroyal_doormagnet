package com.viroyal.doormagnet.usermng.util;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/*
 * Author: Created by qinyl.
 * Date:   2016/12/2.
 * Comments:
 */
public class ShortUUID implements IdentifierGenerator, Configurable {
    private final String sql = "SELECT UUID_SHORT() FROM DUAL";

    public ShortUUID() {
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
    }

    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        try {
            PreparedStatement st = sessionImplementor.getJdbcCoordinator().getStatementPreparer().prepareStatement(sql);
            try {
                ResultSet rs = sessionImplementor.getJdbcCoordinator().getResultSetReturn().extract(st);
                final Long result;
                try {
                    if (!rs.next()) {
                        throw new HibernateException("The database returned no short UUID identity value");
                    }
                    result = Long.parseLong(rs.getString(1));
                } finally {
                    sessionImplementor.getJdbcCoordinator().getResourceRegistry().release(rs, st);
                }
                return result;
            } finally {
                sessionImplementor.getJdbcCoordinator().getResourceRegistry().release(st);
                sessionImplementor.getJdbcCoordinator().afterStatementExecution();
            }
        } catch (SQLException sqle) {
            throw new SqlExceptionHelper().getSqlExceptionConverter().convert(sqle, "could not retrieve short_uuid", sql);
        }
    }
}
