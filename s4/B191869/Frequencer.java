// クラスやインターフェイスをグループ化するため, s4.B191869package宣言
package s4.B191869; 
// Javaの基本的な機能がまとめられたパッケージをインポート
import java.lang.*;
/*package s4.specification;
  ここは、１回、２回と変更のない外部仕様である。
  public interface FrequencerInterface {     // This interface provides the design for frequency counter.
  void setTarget(byte  target[]); // set the data to search.
  void setSpace(byte  space[]);  // set the data to be searched target from.
  int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
  // Otherwise, it return 0, when SPACE is not set or SPACE's length is zero
  // Otherwise, get the frequency of TARGET in SPACE
  int subByteFrequency(int start, int end);
  // get the frequency of subByte of target, i.e target[start], target[start+1], ... , target[end-1].
  // For the incorrect value of START or END, the behavior is undefined.
  }
*/
import s4.specification.*;

/*
    Content logic code is referenced from github account https://github.com/tten5/2021informationQuantity
*/

/*
    FrequencerInterfaceインターフェイスをを継承してFrequencerクラスを定義し、実証する
*/
public class Frequencer implements FrequencerInterface {
    byte [] myTarget; // myTargetを格納するため、データ型Byte配列を宣言
    byte [] mySpace; // mySpaceを格納するため、データ型Byte配列を宣言
    boolean targetReady = false; // target set状態を初期化する
    boolean spaceReady = false; // space set状態を初期化する
    int [] suffixArray; // Suffix Arrayの実装に使うデータの型をint[]

    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.                                    
    // Each suffix is expressed by a integer, which is the starting position in mySpace. 
                            
    // The following is the code to print the contents of suffixArray.
    // This code could be used on debugging.                                                                

    // この関数は、デバッグに使ってもよい。mainから実行するときにも使ってよい。
    // リポジトリにpushするときには、mainメッソド以外からは呼ばれないようにせよ。
    //
    private void printSuffixArray() {
        // 比較対象SuffixArrayの要素をプリントアウトするための関数
        if (spaceReady) {
            for (int i = 0; i < mySpace.length; i++) {
                int s = suffixArray[i];
                System.out.printf("suffixArray[%2d]=%2d:", i, s);
                for (int j = s; j < mySpace.length; j++) {
                    System.out.write(mySpace[j]);
                }
                System.out.write('\n');
            }
        }
    }

    private int suffixCompare(int i, int j) {
        /* Input: 
            @Param{int i}: suffix_iのサイズの計算に使うインデックス
            @Param{int j}: suffix_jのサイズの計算に使うインデックス
        */
        // suffixCompareはソートのための比較メソッドである。
        // 次のように定義せよ。
        //
        // comparing two suffixes by dictionary order.
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD", 
        // suffix_2 is "CD", and sufffix_3 is "D".
        // Each i and j denote suffix_i, and suffix_j.                            
        // Example of dictionary order                                            
        // "i"      <  "o"        : compare by code                              
        // "Hi"     <  "Ho"       ; if head is same, compare the next element    
        // "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big  
        //  
        //The return value of "int suffixCompare" is as follows. 
        // if suffix_i > suffix_j, it returns 1   
        // if suffix_i < suffix_j, it returns -1  
        // if suffix_i = suffix_j, it returns 0;   

        // ここにコードを記述せよ 
        //                     

        int len_i = mySpace.length - i; // length of suffix_i
        int len_j = mySpace.length - j; // length of suffix_j
        int index = 0; // index to check each character in suffix_i and suffix_j

        while (index < len_i && index < len_j) {
            if (mySpace[i + index] > mySpace[j + index])
                return 1;
            if (mySpace[i + index] < mySpace[j + index])
                return -1;
            index++;
        }

        if (len_i > len_j)
            return 1;
        if (len_i < len_j)
            return -1;
        return 0;
    }

    /*
        setSpace == nullまたはsetSpace.length < 1の場合
        setSpaceを格納するのができなかったか、setSpaceが空であるため、spaceReady = falseになる。いわゆる、比較処理を実行できない
        その他の場合はspaceReady = true。いわゆる、比較処理を実行できる
    */
    public void setSpace(byte [] space) { 
        /* Input: 
            @Param{byte [] space}: 比較要素配列
        */
        // suffixArrayの前処理は、setSpaceで定義。
        mySpace = space; 
        // mySpaceの値を”null”いわゆる要素のないmySpace配列の時、mySpaceを格納するのができなかったため、比較処理を実行しない
        if (mySpace == null || mySpace.length < 1) {
            spaceReady = false;
            return;
        } else {
            spaceReady = true;            
        }
        // First, create unsorted suffix array.
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray.
        for (int i = 0; i < space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.      
        }
        //                                            
        // ここに、int suffixArrayをソートするコードを書け。
        // もし、mySpace が"ABC"ならば、
        // suffixArray = { 0, 1, 2} となること求められる。
        // このとき、printSuffixArrayを実行すると
             // suffixArray[ 0]= 0:ABC
        // suffixArray[ 1]= 1:BC
        // suffixArray[ 2]= 2:C
        // のようになるべきである。
        // もし、mySpace が"CBA"ならば
        // suffixArray = { 2, 1, 0} となることが求めらる。
        // このとき、printSuffixArrayを実行すると
        // suffixArray[ 0]= 2:A
        // suffixArray[ 1]= 1:BA
        // suffixArray[ 2]= 0:CBA
        // のようになるべきである。
        // use merge_sort to sort suffixArray  
        merge_sort(suffixArray, 0, space.length - 1);
    }

    private void merge(int arr[], int l, int m, int r) {
        /* Input: 
            @Param{int arr[]}: ソートされたサブ配列 
            @Param{int l}: 左側のファーストインデックス
            @Param{int m}: 中点インデックス
            @Param{int r}: 右側のラストインデックス
        */

        /*
            サブ配列のサイズを宣言する
            左側のサブ配列のサイズは最初のインデックスから中央のインデックスまでの長さに等しい。
            最初のインデックスはゼロであるため、左側のサブ配列のサイズに1を追加する必要がある。
            右側のサブ配列のサイズは中央のインデックスから最後のインデックスまでの長さに等しい。
        */
        int n1 = m - l + 1; // 左側のサブ配列のサイズ
        int n2 = r - m; // 右側のサブ配列のサイズ

        /* 
            マージするため、一時的のデータ型をint L[], R[]を生成する
        */
        int L[] = new int[n1]; // 左側のサブ配列
        int R[] = new int[n2]; // 左側のサブ配列

        /* 
            データを一時配列にコピーする
            各配列の長さが指定され、各要素をそれぞれ新しい配列に保存する
        */
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];

        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        /* 
            一時配列をマージし、値を元の配列に格納する
        */
        int i = 0; // 比較するため、左側配列のインデックス
        int j = 0; // 比較するため、右側配列のインデックス
        int k = l; // マージされた配列のインデックス

        /*
            2つのサブ配列の各要素を比較する
            これら2つのサブ配列のうちのどちらか1つの最後のインデックスと比較すると終了します。
            suffixCompareを使用し、比較する
            suffixCompare(L[i], R[j]) == -1またはsuffixCompare(L[i], R[j]) == 0であれば、その時点の元配列の要素は左側配列L[]の要素を同一である
            その他の場合、その時点の元配列の要素は右側配列R[]の要素を同一である
            また左側配列L[]に残りの要素があれば、残り全部元の配列を保存する
            そして、右側配列R[]に残りの要素があれば、残り全部元の配列を保存する
        */
        while (i < n1 && j < n2) {
            if (suffixCompare(L[i], R[j]) == -1 || suffixCompare(L[i], R[j]) == 0) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        // 側配列L[]に残りの要素があれば、残り全部元の配列を保存する
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        // 右側配列R[]に残りの要素があれば、残り全部元の配列を保存する
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    // merge_sort()
    private void merge_sort(int arr[], int l, int r) {
        /* Input: 
            @Param{int arr[]}: ソートされたサブ配列 
            @Param{int l}: 左側のファーストインデックス
            @Param{int r}: 右側のラストインデックス
        */

        if (l < r) {
            // ソートするに使う中点のインデックスを計算する
            int m = (l + r) / 2;

            // Sort first and second halves
            merge_sort(arr, l, m);
            merge_sort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }
    // ここから始まり、指定する範囲までは変更してはならないコードである。
        
    /*
        myTarget == nullまたはmyTarget.length < 1の場合
        myTargetを格納するのができなかったか、myTargetが空であるため、targetReady = falseになる。いわゆる、比較処理を実行できない
        その他の場合はtargetReady = true。いわゆる、比較処理を実行できる
    */
    public void setTarget(byte [] target) {
         /* Input: 
            @Param{byte [] target}: 比較対象配列
        */
        myTarget = target; 

        if (myTarget == null || myTarget.length < 1) {
            targetReady = false; 
        } else {
            targetReady = true;            
        }
    }

    /*
        targetに対して、spaceに応じて、各要素の出現頻度を返す
        もしtargetReady = falseの場合、−１を返す。いわゆる比較要素がない
        もしspaceReady = falseの場合、０を返す。いわゆる比較対象がない
    */
    public int frequency() {
        if (!targetReady) {
            return -1;
        }
        if (!spaceReady) {
            return 0;
        }        
        return subByteFrequency(0, myTarget.length);
    }

    public int subByteFrequency(int start, int end) {
        // start, and end specify a string to search in myTarget,
        // if myTarget is "ABCD", 
        // start=0, and end=1 means string "A".
        // start=1, and end=3 means string "BC".
        // This method returns how many the string appears in my Space.
        // 
        /* This method should be work as follows, but much more efficient.
           int spaceLength = mySpace.length;                      
           int count = 0;                                        
           for(int offset = 0; offset< spaceLength - (end - start); offset++) {
            boolean abort = false; 
            for(int i = 0; i< (end - start); i++) {
             if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
            }
            if(abort == false) { count++; }
           }
        */
        // The following the counting method using suffix array.
        // 演習の内容は、適切なsubByteStartIndexとsubByteEndIndexを定義することである。
        int first = subByteStartIndex(start, end);
        int last = subByteEndIndex(start, end);
        return last - first;
    }
    // 変更してはいけないコードはここまで。

    private int targetCompare(int i, int j, int k) {
        // subByteStartIndexとsubByteEndIndexを定義するときに使う比較関数。
        // 次のように定義せよ。
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD", 
        // suffix_2 is "CD", and sufffix_3 is "D".
        // target_j_k is a string in myTarget start at j-th postion ending k-th position.
        // if myTarget is "ABCD", 
        // j=0, and k=1 means that target_j_k is "A".
        // j=1, and k=3 means that target_j_k is "BC".
        // This method compares suffix_i and target_j_k.
        // if the beginning of suffix_i matches target_j_k, it return 0.
        // if suffix_i > target_j_k it return 1; 
        // if suffix_i < target_j_k it return -1;
        // if first part of suffix_i is equal to target_j_k, it returns 0;
        //
        // Example of search 
         // suffix target
        // "o" > "i"
        // "o" < "z"
        // "o" = "o"
        // "o" < "oo"
        // "Ho" > "Hi"
        // "Ho" < "Hz"
        // "Ho" = "Ho"
        // "Ho" < "Ho " : "Ho " is not in the head of suffix "Ho"
        // "Ho" = "H" : "H" is in the head of suffix "Ho"
        // The behavior is different from suffixCompare on this case.
        // For example,
        // if suffix_i is "Ho Hi Ho", and target_j_k is "Ho",
        // targetCompare should return 0;
        // if suffix_i is "Ho Hi Ho", and suffix_j is "Ho",
        // suffixCompare should return 1.
        //
        // ここに比較のコードを書け 
        //

        int len_suffix_i = mySpace.length - i; // length of suffix_i
        int len_target = k - j; // length of sub array of myTarget
        int index = 0; // index to check each character in suffix_i and target_j_k

        while (index < len_suffix_i && index < len_target) {
            if (mySpace[i + index] > myTarget[j + index])
                return 1;
            if (mySpace[i + index] < myTarget[j + index])
                return -1;
            index++;
        }

        // if the code reach this line, it means the first part of suffix_i is equal to target_j_k  
        if (len_suffix_i < len_target)
            return -1;
        
        // returns 0 if length of suffix_i >= length of target_j_k   
        return 0;
    }

    // 最初のインデックスを検索する
    private int firstIndexSearch(int low, int high, int start, int end) {
        if (high >= low) {
            int mid = low + (high - low) / 2;
            // if ((mid == 0 || target > arr[mid - 1]) && arr[mid] == target)
            if ((mid == 0 || (targetCompare(suffixArray[mid - 1], start, end) == -1))
                    && (targetCompare(suffixArray[mid], start, end) == 0))
                return mid;
            // else if (target > arr[mid])
            else if (targetCompare(suffixArray[mid], start, end) == -1)
                // search higher half of the arr
                return firstIndexSearch((mid + 1), high, start, end);
            else
                // search lower half of the arr
                return firstIndexSearch(low, (mid - 1), start, end);
        }
        return -1;
    }

    // ラストのインデックスを検索する
    private int lastIndexSearch(int low, int high, int start, int end) {
        if (high >= low) {
            int mid = low + (high - low) / 2;
            // if ((mid == n - 1 || target < arr[mid + 1]) && arr[mid] == target)
            if ((mid == suffixArray.length - 1 || (targetCompare(suffixArray[mid + 1], start, end) == 1))
                    && (targetCompare(suffixArray[mid], start, end) == 0))
                return mid;
            // else if (target < arr[mid])
            else if (targetCompare(suffixArray[mid], start, end) == 1)
                // search lower half of the arr
                return lastIndexSearch(low, (mid - 1), start, end);
            else
                // search higher half of the arr
                return lastIndexSearch((mid + 1), high, start, end);
        }
        return -1;
    }

    private int subByteStartIndex(int start, int end) {
        // suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
        // 以下のように定義せよ。
        // The meaning of start and end is the same as subByteFrequency.
        /* Example of suffix created from "Hi Ho Hi Ho"
           0: Hi Ho
           1: Ho
           2: Ho Hi Ho
           3:Hi Ho
           4:Hi Ho Hi Ho
           5:Ho
           6:Ho Hi Ho
           7:i Ho
           8:i Ho Hi Ho
           9:o
          10:o Hi Ho
        */

        // It returns the index of the first suffix 
        // which is equal or greater than target_start_end.                         
	   // Suppose target is set "Ho Ho Ho Ho"
        // if start = 0, and end = 2, target_start_end is "Ho".
        // if start = 0, and end = 3, target_start_end is "Ho ".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                 
        // if target_start_end is "Ho", it will return 5.                           
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                 
        // if target_start_end is "Ho ", it will return 6.                
        //                                                                          
        // ここにコードを記述せよ。                                                 
        //                                                                         

        return firstIndexSearch(0, suffixArray.length - 1, start, end);       
    }

    private int subByteEndIndex(int start, int end) {
        // suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド
        // 以下のように定義せよ。
        // The meaning of start and end is the same as subByteFrequency.
        /* Example of suffix created from "Hi Ho Hi Ho"
           0: Hi Ho                                    
           1: Ho                                       
           2: Ho Hi Ho                                 
           3:Hi Ho                                     
           4:Hi Ho Hi Ho                              
           5:Ho                                      
           6:Ho Hi Ho                                
           7:i Ho                                    
           8:i Ho Hi Ho                              
           9:o                                       
          10:o Hi Ho                                 
        */
        // It returns the index of the first suffix 
        // which is greater than target_start_end; (and not equal to target_start_end)   
        // Suppose target is set "High_and_Low",
        // if start = 0, and end = 2, target_start_end is "Hi".
        // if start = 1, and end = 2, target_start_end is "i".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                   
        // if target_start_end is "Ho", it will return 7 for "Hi Ho Hi Ho".  
        // Assuming the suffix array is created from "Hi Ho Hi Ho",          
        // if target_start_end is"i", it will return 9 for "Hi Ho Hi Ho".    
        //                                                                   
        // ここにコードを記述せよ                                           
        //                                                                   
        
        int result = lastIndexSearch(0, suffixArray.length - 1, start, end);
        if (result == -1)
            return result;
        // because the finished position is the next position of the last one 
        return result + 1; 
    }

    // Suffix Arrayを使ったプログラムのホワイトテストは、
    // privateなメソッドとフィールドをアクセスすることが必要なので、
    // クラスに属するstatic mainに書く方法もある。
    // static mainがあっても、呼びださなければよい。
    // 以下は、自由に変更して実験すること。
    // 注意：標準出力、エラー出力にメッセージを出すことは、
    // static mainからの実行のときだけに許される。
    // 外部からFrequencerを使うときにメッセージを出力してはならない。
    // 教員のテスト実行のときにメッセージがでると、仕様にない動作をするとみなし、
    // 減点の対象である。
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try {
            // テストに使うのに推奨するmySpaceの文字は、"ABC", "CBA", "HHH", "Hi Ho Hi Ho".
            // Test case 1: mySpaceの文字は、"ABC"
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("ABC".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("B".getBytes());
            // **** Print out subByteStartIndex, and subByteEndIndex
            int sub_start1 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex = " + sub_start1 + " ");
            int sub_end1 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex = " + sub_end1 + " ");
            int result1 = frequencerObject.frequency();
            System.out.print("Freq = " + result1 + " ");
            if (1 == result1) {
                System.out.println("OK");
            } else {
                System.out.println("WRONG");
            }

            // Test case 2: mySpaceの文字は、"CBA",
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("CBA".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("CB".getBytes());
            // **** Print out subByteStartIndex, and subByteEndIndex
            int sub_start2 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex = " + sub_start2 + " ");
            int sub_end2 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex = " + sub_end2 + " ");
            int result2 = frequencerObject.frequency();
            System.out.print("Freq = " + result2 + " ");
            if (1 == result2) { 
                System.out.println("OK"); 
            } else {
                System.out.println("WRONG"); 
            }

            // Test case 3: mySpaceの文字は、"HHH"
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("HHH".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("H".getBytes());
            // **** Print out subByteStartIndex, and subByteEndIndex
            int sub_start3 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex = " + sub_start3 + " ");
            int sub_end3 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex = " + sub_end3 + " ");
            int result3 = frequencerObject.frequency();
            System.out.print("Freq = " + result3 + " ");
            if (3 == result3) { 
                System.out.println("OK"); 
            } else {
                System.out.println("WRONG"); 
            }

            // Test case 4: mySpaceの文字は、"Hi Ho Hi Ho"
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray();
            /* Example from "Hi Ho Hi Ho"    
               0: Hi Ho                      
               1: Ho                         
               2: Ho Hi Ho                   
               3:Hi Ho                       
               4:Hi Ho Hi Ho                 
               5:Ho                          
               6:Ho Hi Ho
               7:i Ho                        
               8:i Ho Hi Ho                  
               9:o                           
              10:o Hi Ho                     
            */

            frequencerObject.setTarget("H".getBytes());
            // **** Print out subByteStartIndex, and subByteEndIndex

            int sub_start4 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex = " + sub_start4 + " ");
            int sub_end4 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex = " + sub_end4 + " ");
            int result4 = frequencerObject.frequency();
            System.out.print("Freq = " + result4 + " ");
            if (4 == result4) { 
                System.out.println("OK"); 
            } else {
                System.out.println("WRONG"); 
            }
        } catch (Exception e) {
            System.out.println("STOP");
        }
    }
}