package dev.yua.lynxiberian.repositories;

import dev.yua.lynxiberian.models.TwitterAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TwitterAccountRepository extends CrudRepository<TwitterAccount, Long> {
}
