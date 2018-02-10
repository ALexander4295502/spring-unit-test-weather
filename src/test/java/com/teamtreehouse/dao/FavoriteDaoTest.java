package com.teamtreehouse.dao;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert .*;
import static com.teamtreehouse.domain.Favorite.FavoriteBuilder;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.teamtreehouse.Application;
import com.teamtreehouse.domain.Favorite;
import com.teamtreehouse.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@DatabaseSetup("classpath:favorites.xml")
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
public class FavoriteDaoTest {

  public static final String PLACE_ID = "treeaasdasdh";

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private FavoriteDao dao;

  @Before
  public void setup() {
    User user = new User();
    user.setId(1L);
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
  }

  @Test
  public void findAll_ShouldReturnTwo() throws Exception {
    assertThat(dao.findAll(), hasSize(2));
  }

  @Test
  public void save_ShouldPersistEntity() throws Exception {
    Favorite favorite = new FavoriteBuilder().withPlaceId(PLACE_ID).build();
    dao.saveForCurrentUser(favorite);
    assertThat(dao.findByPlaceId(PLACE_ID), notNullValue(Favorite.class));
  }
}