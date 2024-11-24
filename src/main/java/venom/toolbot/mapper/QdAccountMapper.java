package venom.toolbot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import venom.toolbot.entity.QdAccount;

import java.util.List;

@Mapper
public interface QdAccountMapper {
    int deleteByPrimaryKey(Long accountId);

    int insert(QdAccount record);

    int insertSelective(QdAccount record);

    QdAccount selectByPrimaryKey(Long accountId);

    int updateByPrimaryKeySelective(QdAccount record);

    int updateByPrimaryKey(QdAccount record);

    List<QdAccount> selectAll();

    boolean existsById(@Param("id") Long id);
}