import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.util.Arrays;
import static org.mockito.Mockito.*;

public class ReducerTest {

  @Mock
  private Reducer<Text, IntWritable, Text, IntWritable>.Context mockContext;

  private WordCount.IntSumReducer reducer;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    reducer = new WordCount.IntSumReducer();
  }

  @Test
  public void testReduce() throws IOException, InterruptedException {
    Text key = new Text("test");
    Iterable<IntWritable> values = Arrays.asList(new IntWritable(1), new IntWritable(2), new IntWritable(3));

    reducer.reduce(key, values, mockContext);

    verify(mockContext).write(key, new IntWritable(6));
  }
}
