package s4.B213376; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/*
interface FrequencerInterface {     // This interface provides the design for frequency counter.
    void setTarget(byte[]  target); // set the data to search.
    void setSpace(byte[]  space);  // set the data to be searched target from.
    int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
                    //Otherwise, it return 0, when SPACE is not set or Space's length is zero
                    //Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
}
*/

/*
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/


public class TestCase {
    static boolean success = true;
   
    public static void main(String[] args) {

	try {
	    FrequencerInterface  myObject;
	    int freq;
	    System.out.println("checking Frequencer");
	    // This is smoke test
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("H".getBytes());
	    freq = myObject.frequency();
	    assert freq == 4: "Hi Ho Hi Ho, H: " + freq;

        // Frequency() Check
		myObject = new Frequencer();
        freq = myObject.frequency();
        assert freq == -1 : "Not returning -1 when Target is not set and Space is not set";
        
		myObject = new Frequencer();
		myObject.setTarget("".getBytes());
        freq = myObject.frequency();
        assert freq == -1 : "Not returning -1 when Target's length is zero and Space is not set";

        myObject = new Frequencer();
        myObject.setSpace("Hi Ho Hi Ho".getBytes());
        freq = myObject.frequency();
        assert freq == -1 : "Not returning -1 when Target is not set";
        
		myObject = new Frequencer();
        myObject.setSpace("Hi Ho Hi Ho".getBytes());
		myObject.setTarget("".getBytes());
        freq = myObject.frequency();
        assert freq == -1 : "Not returning -1 when Target's length is zero";

        myObject = new Frequencer();
        myObject.setTarget("Ho".getBytes());
        freq = myObject.frequency();
        assert freq == 0 : "Not returning 0 when Space is not set and Target is not empty";
        
		myObject = new Frequencer();
        myObject.setSpace("".getBytes());
		myObject.setTarget("Ho".getBytes());
        freq = myObject.frequency();
        assert freq == 0 : "Not returning 0 when Space is empty";

        myObject = new Frequencer();
        myObject.setSpace("Hi Ho Hi Ho".getBytes());
        myObject.setTarget("Hi Ho Hi Hoo".getBytes());
        freq = myObject.frequency();
        assert freq == 0 : "Not returning 0 when Target's length is longer than Space's length";
        
        myObject = new Frequencer();
        myObject.setSpace("Hi Ho Hi Ho".getBytes());
        myObject.setTarget("Hoo".getBytes());
        freq = myObject.frequency();
        assert freq == 0 : "Not returning 0 when Target not found";
		
		myObject = new Frequencer();
        myObject.setSpace("Hi Ho Hi Ho".getBytes());
        myObject.setTarget("ho".getBytes());
        freq = myObject.frequency();
        assert freq == 0 : "Not returning 0 when Target not found";

        myObject = new Frequencer();
        myObject.setSpace("Hi Ho Hi Ho".getBytes());
        myObject.setTarget("Hi Ho".getBytes());
        freq = myObject.frequency();
        assert freq == 2 : "Frequency for Hi Ho in Hi Ho Hi Ho should return 2.0. But it returns" + freq;
        
		myObject = new Frequencer();
        myObject.setSpace("Hi Ho Hi Ho".getBytes());
        myObject.setTarget("H".getBytes());
        freq = myObject.frequency();
        assert freq == 4 : "Frequency for H in Hi Ho Hi Ho should return 4.0. But it returns" + freq;
        
        myObject = new Frequencer();
        myObject.setSpace("jr jx jr jxrjx".getBytes());
        myObject.setTarget("jx".getBytes());
        freq = myObject.frequency();
        assert freq == 3 : "Frequency for jx in jr jx jr jxrjx should return 3.0. But it returns" + freq;
        
		myObject = new Frequencer();
	    myObject.setSpace("AAA".getBytes());
	    myObject.setTarget("A".getBytes());
	    freq = myObject.frequency();
		assert freq == 3 : "Frequency for A in AAA should return 3. But it returns"+freq;
	    
		myObject = new Frequencer();
	    myObject.setSpace("AAA".getBytes());
	    myObject.setTarget("AA".getBytes());
	    freq = myObject.frequency();
	    assert freq == 2 : "Frequency for AA in AAA should return 2. But it returns"+freq;
	    
		myObject = new Frequencer();
	    myObject.setSpace("AAA".getBytes());
	    myObject.setTarget("AAA".getBytes());
	    freq = myObject.frequency();  
		assert freq == 1 : "Frequency for AAA in AAA should return 1. But it returns"+freq;
	    
		// subByteFreqency(0,0) is illegal 
		// subByteFrequency() Check
		myObject = new Frequencer();
	    myObject.setSpace("ABACADAE".getBytes());
	    myObject.setTarget("AABBCCDD".getBytes());
	    freq = myObject.subByteFrequency(0,1);
	    assert freq == 4 : "SubBytefrequency for ABACADAE should return 4 when Target is AABBCCDD[0:1]. But it returns "+freq;
	    
		myObject = new Frequencer();
	    myObject.setSpace("ABACADAE".getBytes());
	    myObject.setTarget("AABBCCDD".getBytes());
	    freq = myObject.subByteFrequency(0,8);
	    assert freq == 0 : "SubBytefrequency for ABACADAE should return 0 when Target is AABBCCDD[0:8]. But it returns "+freq;
	    
		myObject = new Frequencer();
	    myObject.setSpace("ABACADAE".getBytes());
	    myObject.setTarget("AABBCCDD".getBytes());
	    freq = myObject.subByteFrequency(1,3);
	    assert freq == 1 : "SubBytefrequency for ABACADAE should return 1 when Target is AABBCCDD[1:3]. But it returns "+freq;
	    
        System.out.println("Frequencer Test Done");
	}
	catch(Exception e) {
	    System.out.println("Exception occurred in Frequencer Object");
	    success = false;
	}

	try {
	    InformationEstimatorInterface myObject;
	    double value;
	    System.out.println("checking InformationEstimator");
	    myObject = new InformationEstimator();
	    myObject.setSpace("3210321001230123".getBytes());
	    myObject.setTarget("0".getBytes());
	    value = myObject.estimation();
	    assert (value > 1.9999) && (2.0001 >value): "IQ for 0 in 3210321001230123 should be 2.0. But it returns "+value;
	    myObject.setTarget("01".getBytes());
	    value = myObject.estimation();
	    assert (value > 2.9999) && (3.0001 >value): "IQ for 01 in 3210321001230123 should be 3.0. But it returns "+value;
	    myObject.setTarget("0123".getBytes());
	    value = myObject.estimation();
	    assert (value > 2.9999) && (3.0001 >value): "IQ for 0123 in 3210321001230123 should be 3.0. But it returns "+value;
	    myObject.setTarget("00".getBytes());
	    value = myObject.estimation();
	    assert (value > 3.9999) && (4.0001 >value): "IQ for 00 in 3210321001230123 should be 4.0. But it returns "+value;
	    myObject.setTarget("01230123".getBytes());
	    value = myObject.estimation();
	    assert (value > 3.9999) && (4.0001 >value): "IQ for 01230123 in 3210321001230123 should be 4.0. But it returns "+value;
	}
	catch(Exception e) {
	    System.out.println("Exception occurred in InformationEstimator Object");
	    success = false;
	}
    
	if(success) { System.out.println("TestCase OK"); } 
    
	}
}	    
	    
