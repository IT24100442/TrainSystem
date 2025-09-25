package org.example.trainsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.example.trainsystem.entity.Train;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TrainDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public class TrainRowMapper implements RowMapper<Train> {
        @Override
        public Train mapRow(ResultSet rs, int rowNum) throws SQLException {
            Train train = new Train();
            train.setTid(rs.getInt("trainId"));
            train.setName(rs.getString("trainName"));
            return train;
        }
    }

    public void save(Train train) {
        String sql = "INSERT INTO Train (trainName) VALUES (?)";
        jdbcTemplate.update(sql, train.getName());
    }

    public Train getTrainById(int trainId) {
        String sql = "SELECT * FROM Train WHERE trainId = ?";
        return jdbcTemplate.queryForObject(sql, new TrainRowMapper(), trainId);
    }

    public List<Train> getAllTrains() {
        String sql = "SELECT * FROM Train";
        return jdbcTemplate.query(sql, new TrainRowMapper());
    }

    public int updateTrain(Train train) {
        String sql = "UPDATE Train SET trainName = ? WHERE trainId = ?";
        return jdbcTemplate.update(sql, train.getName(), train.getTid());
    }

    public int getLastTrainId() {
        String sql = "SELECT TOP 1 trainId FROM Train ORDER BY trainId DESC";
        Integer lastId = jdbcTemplate.queryForObject(sql, Integer.class);
        return lastId != null ? lastId : 0;
    }

    public int deleteTrain(int trainId) {
        String sql = "DELETE FROM Train WHERE trainId = ?";
        return jdbcTemplate.update(sql, trainId);
    }
}


