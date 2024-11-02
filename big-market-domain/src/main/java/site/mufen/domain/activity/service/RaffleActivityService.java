package site.mufen.domain.activity.service;

import org.springframework.stereotype.Service;
import site.mufen.domain.activity.repository.IActivityRepository;

/**
 * @author mufen
 * @Description
 * @create 2024/11/1 20:48
 */
@Service
public class RaffleActivityService extends AbstractRaffleActivity{

    public RaffleActivityService(IActivityRepository activityRepository) {
        super(activityRepository);
    }
}
