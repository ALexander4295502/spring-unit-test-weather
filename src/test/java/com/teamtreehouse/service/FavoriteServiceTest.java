package com.teamtreehouse.service;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teamtreehouse.dao.FavoriteDao;
import com.teamtreehouse.domain.Favorite;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FavoriteServiceTest {

  @Mock
  private FavoriteDao dao;

  @InjectMocks
  private FavoriteService service = new FavoriteServiceImpl();

  @Test
  public void findAll_ShouldReturnTwo() throws Exception {
    List<Favorite> favorites = Arrays.asList(
        new Favorite(),
        new Favorite()
    );

    when(dao.findAll()).thenReturn(favorites);
    assertEquals(
        "findAll should return two favorites",
        2,
        service.findAll().size()
    );
    verify(dao).findAll();
  }

  @Test
  public void findById_ShouldReturnOne() throws Exception {
    final long FIND_ONE_ID = 1L;
    when(dao.findOne(FIND_ONE_ID)).thenReturn(new Favorite());
    assertThat(service.findById(FIND_ONE_ID), instanceOf(Favorite.class));
    verify(dao).findOne(FIND_ONE_ID);
  }

  @Test(expected = FavoriteNotFoundException.class)
  public void findById_ShouldThrowFavoriteNotFoundException() throws Exception {
    final long FIND_ONE_ID = 1L;
    when(dao.findOne(FIND_ONE_ID)).thenReturn(null);
    service.findById(FIND_ONE_ID);
    verify(dao).findOne(FIND_ONE_ID);
  }
}