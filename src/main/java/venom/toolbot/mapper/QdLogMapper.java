package venom.toolbot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import venom.toolbot.entity.QdLog;

import java.util.List;

@Mapper
public interface QdLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QdLog record);

    int insertSelective(QdLog record);

    QdLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QdLog record);

    int updateByPrimaryKey(QdLog record);

    List<QdLog> selectByLoginAccount(@Param("appName") String appName, @Param("account") String loginAccount);
}