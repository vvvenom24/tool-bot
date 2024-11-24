package venom.toolbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import venom.toolbot.entity.QdAccount;
import venom.toolbot.mapper.QdAccountMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QdAccountService {

    private final QdAccountMapper qdAccountMapper;

    public List<QdAccount> findAll() {
        return qdAccountMapper.selectAll();
    }

    public Optional<QdAccount> findById(Long id) {
        return Optional.ofNullable(qdAccountMapper.selectByPrimaryKey(id));
    }

    public QdAccount save(QdAccount account) {
        qdAccountMapper.insertSelective(account);
        return account;
    }

    public QdAccount update(QdAccount account) {
        qdAccountMapper.updateByPrimaryKeySelective(account);
        return account;
    }

    public boolean existsById(Long id) {
        return !qdAccountMapper.existsById(id);
    }

    public void deleteById(Long id) {
        qdAccountMapper.deleteByPrimaryKey(id);
    }
}
