package venom.toolbot.mapper;

import org.apache.ibatis.annotations.Mapper;
import venom.toolbot.entity.QdAccount;

@Mapper
public interface QdAccountMapper {
    int deleteByPrimaryKey(Long accountId);

    int insert(QdAccount record);

    int insertSelective(QdAccount record);

    QdAccount selectByPrimaryKey(Long accountId);

    int updateByPrimaryKeySelective(QdAccount record);

    int updateByPrimaryKey(QdAccount record);
}