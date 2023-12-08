import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.StringTokenizer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MapperTest {

    private WordCount.TokenizerMapper mapper;
    @Mock private Context context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mapper = new WordCount.TokenizerMapper();
    }

    @Test
    public void testMap() throws IOException, InterruptedException {
        Text value = new Text("test input");
        Object key = new Object();

        // Run the map method
        mapper.map(key, value, context);

        // Verify context.write is called correctly
        StringTokenizer itr = new StringTokenizer(value.toString());


        // Verify that context.write was called the correct number of times
        verify(context, times(itr.countTokens())).write(any(Text.class), eq(new IntWritable(1)));
    }
}
