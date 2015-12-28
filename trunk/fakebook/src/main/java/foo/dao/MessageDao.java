package foo.dao;

import org.springframework.stereotype.Repository;

import foo.entity.Message;

/**
 * 負責 message 的資料庫操作
 * @author phil
 */
@Repository
public class MessageDao extends GenericDao<Message, Integer> {
}
