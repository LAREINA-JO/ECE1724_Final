import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/* This is for black-box testing */
public class WordCountTest {

    @Test
    public void testTokenizerMapper() throws IOException, InterruptedException {

        // create a test instance of mapper
        WordCount.TokenizerMapper mapper = new WordCount.TokenizerMapper();

        // mock the Context to simulate the Hadoop environment
        Mapper.Context context = mock(Mapper.Context.class);

        // run the map method with a sample text input
        mapper.map(null, new Text("hello world hello"), context);

        // create ArgumentCaptors to capture the outputs written to the context
        ArgumentCaptor<Text> textCaptor = ArgumentCaptor.forClass(Text.class);
        ArgumentCaptor<IntWritable> intWritableCaptor = ArgumentCaptor.forClass(IntWritable.class);

        // verify the write method was called three times and get arguments
        verify(context, times(3)).write(textCaptor.capture(), intWritableCaptor.capture());

        // assert the first key is "hello" and value is 1
        assertEquals("hello", textCaptor.getAllValues().get(0).toString());
        assertEquals(1, intWritableCaptor.getAllValues().get(0).get());
        // Print a message if the test passes
        System.out.println("Mapper test passed successfully.");
    }

    @Test
    public void testIntSumReducer() throws IOException, InterruptedException {
        // create a test instance of reducer
        WordCount.IntSumReducer reducer = new WordCount.IntSumReducer();

        // mock the Context to simulate the Hadoop environment
        Reducer.Context context = mock(Reducer.Context.class);

        // prepare a key and a list of values to simulate the input to the reducer
        Text key = new Text("hello");
        Iterable<IntWritable> values = Arrays.asList(new IntWritable(1), new IntWritable(1), new IntWritable(1));

        // run the reduce method with the key and values
        reducer.reduce(key, values, context);

        // create ArgumentCaptors to capture the output written to the context
        ArgumentCaptor<Text> textCaptor = ArgumentCaptor.forClass(Text.class);
        ArgumentCaptor<IntWritable> intWritableCaptor = ArgumentCaptor.forClass(IntWritable.class);

        // verify the write method was called and get the arguments
        verify(context).write(textCaptor.capture(), intWritableCaptor.capture());

        // assert that the output key is "hello" and the sum of the values is 3
        assertEquals("hello", textCaptor.getValue().toString());
        assertEquals(3, intWritableCaptor.getValue().get());
        // Print a message if the test passes
        System.out.println("Reducer test passed successfully.");
    }
}
