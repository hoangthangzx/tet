package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new com.metalsensor.gold.detector.finder.DataBinderMapperImpl());
  }
}
