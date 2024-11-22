package venom.toolbot.mapper;

import org.apache.ibatis.annotations.Mapper;
import venom.toolbot.entity.QdLog;

@Mapper
public interface QdLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QdLog record);

    int insertSelective(QdLog record);

    QdLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QdLog record);

    int updateByPrimaryKey(QdLog record);
}