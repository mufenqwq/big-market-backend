package site.mufen.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.persistent.po.UserCreditAccount;

/**
 * @author mufen
 * @Description 用户积分账户Dao
 * @create 2024/11/13 12:18
 */
@Mapper
public interface IUserCreditAccountDao {

    int updateAddAccount(UserCreditAccount userCreditAccountReq);

    void insert(UserCreditAccount userCreditAccountReq);

    UserCreditAccount queryUserCreditAccount(UserCreditAccount userCreditAccountReq);

    void updateSubtractionAccount(UserCreditAccount userCreditAccountReq);
}
