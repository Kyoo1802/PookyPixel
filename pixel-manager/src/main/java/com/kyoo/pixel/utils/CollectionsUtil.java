package com.kyoo.pixel.utils;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

public final class CollectionsUtil {

  public static <T> Collector<T, List<T>, List<T>> inReverse() {
    return Collector.of(
        ArrayList::new,
        (l, t) -> l.add(t),
        (l, r) -> {
          l.addAll(r);
          return l;
        },
        Lists::<T>reverse);
  }
}
