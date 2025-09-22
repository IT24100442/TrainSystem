// Java
package org.example.trainsystem.repository;

import org.example.trainsystem.entity.TrainRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TrainRouteDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<TrainRoute> rowMapper = new RowMapper<>() {
        @Override
        public TrainRoute mapRow(ResultSet rs, int rowNum) throws SQLException {
            TrainRoute route = new TrainRoute();
            route.setTrainRouteId(rs.getInt("trainRouteId"));
            route.setTrainId(rs.getInt("trainId"));
            route.setRouteId(rs.getInt("routeId"));
            route.setActive(rs.getBoolean("active"));

            return route;
        }
    };

    public List<TrainRoute> findAll() {
        String sql = "SELECT * FROM TrainRoute";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public TrainRoute findById(int id) {
        String sql = "SELECT * FROM TrainRoute WHERE trainRouteId = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
    public TrainRoute findbyRouteId(int routeId) {
        String sql = "SELECT * FROM TrainRoute WHERE routeId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, routeId);
        } catch (Exception e) {
            return null;
        }
    }
/*
    public int save(TrainRoute route) {
        String sql = "INSERT INTO TrainRoute (name) VALUES (?)";
        return jdbcTemplate.update(sql, route.getName());
    }
*/
    public int update(TrainRoute route) {
        String sql = "UPDATE TrainRoute SET trainId = ?, routeId = ? WHERE trainRouteId = ?";
        return jdbcTemplate.update(sql, route.getTrainId(), route.getRouteId(), route.getTrainRouteId());
    }

    public int delete(int id) {
        String sql = "DELETE FROM TrainRoute WHERE trainRouteId = ?";
        return jdbcTemplate.update(sql, id);
    }

    public TrainRoute findByTrainId(int trainId) {
        String sql = "SELECT * FROM TrainRoute WHERE trainId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, trainId);
        } catch (Exception e) {
            return null;
        }
    }
}
