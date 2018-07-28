package com.seal.multipledatasource.repo;

import com.seal.multipledatasource.config.UserConfig;
import com.seal.multipledatasource.entity.user.User;
import com.seal.multipledatasource.repo.user.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = UserConfig.class)
@Slf4j
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Before
    public void setUp() throws Exception {
        userRepo.saveAll(Arrays.asList(new User(null, "Alan Turing"),
                new User(null, "Edsger W. Dijkstra")));
    }

    @Test
    @Transactional
    public void testFindAllUser() {
        List<User> userList = userRepo.findAll();

        Assertions.assertThat(userList).isNotEmpty();
        Assertions.assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void testSingleUser() {
        User user = userRepo.save(new User(null, "Muztaba"));

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isGreaterThan(0);
        Assertions.assertThat(user.getName()).isEqualTo("Muztaba");
    }
}