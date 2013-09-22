import java.io.IOException;


/**
 * @author Martin Lundberg-Jensen
 */
public class MainTABU {


    public static void main(String[] args) throws IOException {
        DataObject Data = new DataObject("large_real_data_S_110_10_11_3.dat");
    	//DataObject Data = new DataObject("generated_S2_20_5_4_3.gms_T1.dat");
        TABU tab = new TABU(Data,7);
        tab.Run(30);
        Data.WriteSolution();
        System.out.println("Iterations: "+tab.IterationCount);
        System.out.println("Solution Value: "+Data.CalcSolutionValue(Data.Tables));

    }

}
