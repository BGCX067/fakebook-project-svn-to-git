package foo.dao;

import org.springframework.stereotype.Repository;

import foo.entity.MessageBase;

/**
 * 負責 message base 的資料庫操作
 * @author phil
 */
@Repository
public class MessageBaseDao extends GenericDao<MessageBase, Integer> {
}
