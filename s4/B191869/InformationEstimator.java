// クラスやインターフェイスをグループ化するため, s4.B191869package宣言
package s4.B191869; 
// Javaの基本的な機能がまとめられたパッケージをインポート
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface {
    void setTarget(byte target[]);  // set the data for computing the information quantities
    void setSpace(byte space[]);  // set data for sample space to computer probability
    double estimation();  // It returns 0.0 when the target is not set or Target's length is zero;
    // It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
    // The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
    // Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
    // Otherwise, estimation of information quantity,
}
*/

/*
    FrequencerInterfaceインターフェイスをを継承してInformationEstimatorクラスを定義し、実証する
*/
public class InformationEstimator implements InformationEstimatorInterface {
    static boolean debugMode = false;
    byte[] myTarget; // Data to compute its information quantity
    byte[] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency
    double[] suffixEstimation; // Store the estimation of substrings

    private void showVariables() {
        for(int i = 0; i < mySpace.length; i++) { 
            System.out.write(mySpace[i]); 
        }
        System.out.write(' ');

        for(int i = 0; i< myTarget.length; i++) { 
            System.out.write(myTarget[i]); 
        }        
        System.out.write(' ');
    }

    byte[] subBytes(byte[] x, int start, int end) {
        // Corresponding to substring of String for byte[],
        // It is not implement in class library because internal structure of byte[] requires copy.
        byte[] result = new byte[end - start];
        for(int i = 0; i < end - start; i++) { 
            result[i] = x[start + i]; 
        };
        return result;
    }

    // IQ: information quantity for a count, -log2(count/sizeof(space))
    double iq (int freq) {
        if (freq == 0) {
            return Double.MAX_VALUE;
        }
        return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    @Override
    public void setTarget(byte[] target) {
        myTarget = target;
    }

    @Override
    public void setSpace(byte[] space) {
        myFrequencer = new Frequencer();
        mySpace = space;
        myFrequencer.setSpace(space);
    }

    @Override
    public double estimation(){
        // Returns 0.0 when the target is not set or Target's length is zero;
        if(myTarget == null || myTarget.length == 0) { 
            return (double) 0.0; 
        }
        // Returns Double.MAX_VALUE when the true value is infinite or space is not set 
        if (mySpace == null || mySpace.length == 0) { 
            return Double.MAX_VALUE; 
        }

        if(debugMode) { 
            showVariables(); 
        }

        // iqの頻度を保存するに使うsuffixEstimation[]配列を宣言する
        // suffixEstimation[]配列のサイズはmyTarget[]配列と等しい
        suffixEstimation = new double[myTarget.length];
        
        /*
            suffixEstimation[]配列のファーストインデックスのiq値計算する
        */
        myFrequencer.setTarget(subBytes(myTarget, 0, 1));
        suffixEstimation[0] = iq(myFrequencer.frequency());

        /*
            残り全てのサブ文字列から最小値を探し、suffixEstimation配列に格納する
            最小のiqをminIqとして初期化し、
            iはエンドインデックスとし、
            jはスタットインデックスとし、
            suffixEstimation[]の要素がなくなるまで、i、jは１を増やし、比較するため、ループを作成する
            それぞれ、myFrequencer頻度を取得し、IQ値をcurrentIqとして計算する
            もしminIq > (suffixEstimation[j] + currentIqであれば minIq = suffixEstimation[j] + currentIq
            最後に得た値をsuffixEstimation[]に格納する
        */
        for(int i = 1; i < suffixEstimation.length; i++){
            myFrequencer.setTarget(subBytes(myTarget, 0, i + 1));
            double minIq = iq(myFrequencer.frequency()); 

            for(int j = 0; j < i; j++){
                myFrequencer.setTarget(subBytes(myTarget, j + 1, i + 1));
                
                double currentIq = iq(myFrequencer.frequency());
                if(minIq > (suffixEstimation[j] + currentIq)) {
                    minIq = suffixEstimation[j] + currentIq;
                }
            }
            suffixEstimation[i] = minIq;
        }

        if(debugMode) { 
            System.out.printf("\testimation = %5.5f\n", suffixEstimation[myTarget.length - 1]); 
        }

        return suffixEstimation[myTarget.length - 1];
    }

    public static void main(String[] args) {
        InformationEstimator myObject;
        double value;
        debugMode = true;

        myObject = new InformationEstimator();
        myObject.setSpace("3210321001230123".getBytes());
        myObject.setTarget("0".getBytes());
        value = myObject.estimation();
        myObject.setTarget("01".getBytes());
        value = myObject.estimation();
        myObject.setTarget("0123".getBytes());
        value = myObject.estimation();
        myObject.setTarget("00".getBytes());
        value = myObject.estimation();
    }
}