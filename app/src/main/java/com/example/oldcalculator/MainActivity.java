package com.example.oldcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//GitHub test Edit GitHub
import com.example.oldcalculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    boolean isFirstInput=true;//처음 입력인가 검사
    boolean isOperatorClick=false;//연산자가 클릭되었나 검사(=을 계속 누를 때 처리)

    double resultNumber=0;//계산 결과
    double inputNumber=0;//입력값

    String operator="＋";//연산자
    String lastOperator="＋";//=를 사용했을 때 마지막 연산자

    ActivityMainBinding activityMainBinding;//Binding 사용 준비

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding=ActivityMainBinding.inflate(getLayoutInflater());//Binding 객체 생성
        //setContentView(R.layout.activity_main);
        setContentView(activityMainBinding.getRoot());
    }

    public void numButtonClick(View view) {//이것을 버튼의 눌렸을 때 View에 정보가 들어가 있다.
        //Button button = findViewById(view.getId());//view에서 늘려진 해당 버튼의 개체 정보를 받아옴
        //String getButtonText = button.getText().toString();//해당 버튼의 text를 문자열로 받아 준비
       //tag를 구분자로 사용하기- tag 값을 다 넣어주고(위의 식을 아래 한 줄로
        //String getButtonText =view.getTag().toString();//해당 버튼의 tag값을 문자열로 받아 준비
        //아래의 변수에 바로 값을 넣어 주어도 된다.

        if (isFirstInput) {
            //activityMainBinding.resultTextView.setText(getButtonText);//resultTextView에 쓴다
            activityMainBinding.resultTextView.setText(view.getTag().toString());//resultTextView에 쓴다
            //activityMainBinding.mathTextView.setText(view.getTag().toString());//mathTextView에 쓴다
            isFirstInput = false;
            if(operator.equals("=")){
                //activityMainBinding.mathTextView.setText("");
                activityMainBinding.mathTextView.setText(null);
                isOperatorClick=false;

            }
        } else {
            //만약 0이 있는데 다시 0을 입력하면 처리
            if(activityMainBinding.resultTextView.getText().toString().equals("0")){
                Toast.makeText(this, "0으로 시작되는 숫자는 없습니다.", Toast.LENGTH_SHORT).show();
                isFirstInput = true;
            }else{
                //activityMainBinding.resultTextView.append(getButtonText);//resultTextView에 추가된다.
                activityMainBinding.resultTextView.append(view.getTag().toString());//resultTextView에 추가된다.
                //activityMainBinding.mathTextView.append(view.getTag().toString());//mathTextView에 추가된다.
            }
        }
    }

    public void allClearButtonClick(View view){//AC
        activityMainBinding.resultTextView.setText("0");//연산 결과 초기화
        activityMainBinding.mathTextView.setText("0");//연산 순서 초기화
        resultNumber=0;//연산값 0
        operator="＋";//연산자 "＋"로 초기화
        isFirstInput=true;//초기화
        isOperatorClick=false;//연산자 초기화 거짓
    }

    public void pointButtonClick(View view){//소수점
        //두개가 연속되어도 안되고 숫자 앞에 나와도 안됨
        if(isFirstInput){
            activityMainBinding.resultTextView.setText("0"+view.getTag().toString());//소수점을 그냥 쓰기
            isFirstInput=false;//처음이 아닌것으로
        }else {
            if(activityMainBinding.resultTextView.getText().toString().contains(".")){
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }else{
                activityMainBinding.resultTextView.append(view.getTag().toString());//소수점을 추가하기
            }
        }
    }

    public void operatorClick(View view){//.버튼에 연결, 태그 붙여주기
        isOperatorClick=true;//연산자가 클릭되었나 검사(=을 계속 누를 때 처리)

        //여기에서 말고 연산자 클릭에서 설정하기
        //=을 입력했을 때 마지막으로 사용된 연산자 -> 계속 =를 클릭했을 때 사용하기
        //lastOperator=operator;//마지막 연산자에 = 이전에 사용된 연산자를 넣어 기억하기
        lastOperator=view.getTag().toString();

        if(isFirstInput){//=이 눌려진 상태와 연산자가 있는데 또 연산자를 누를 경우
            if(operator.equals("=")){
                operator=view.getTag().toString();//클릭된 연산자
                resultNumber=Double.parseDouble(activityMainBinding.resultTextView.getText().toString());
                //resultTextView의 값을 resultNumber에 넣기
                activityMainBinding.mathTextView.setText(resultNumber+ " " + operator+ " ");
            }else{
                operator=view.getTag().toString();//연산자를 준비
                String getMathText=activityMainBinding.mathTextView.getText().toString();//연산 계산식 문자열 가져오기
                String subString=getMathText.substring(0, getMathText.length()-2);// -2는 (연산자+공백) 2개를 자른 문자열
                activityMainBinding.mathTextView.setText(subString);//공백과 연산자를 자른 내용을 다시 써 준다.
                activityMainBinding.mathTextView.append(operator+ " ");//다시 입력된 연산자와 공백을 붙여 써 준다.
            }


        } else {
            //inputNumber=activityMainBinding.resultTextView.getText().toString();//문자를 double로 변환하지 못해서
            inputNumber=Double.parseDouble(activityMainBinding.resultTextView.getText().toString());//문자열을 더블형으로

            //인수를 주어서 결과값으로 받아 오기
            resultNumber=calculator(resultNumber,inputNumber,operator);//메소드를 만들어 사용

            activityMainBinding.resultTextView.setText(String.valueOf(resultNumber));//연산 결과 값
            isFirstInput=true;//다시 입력이 초기화
            operator=view.getTag().toString();//태그에 등록된 값을 연산자에 넣는다.
            activityMainBinding.mathTextView.append(inputNumber+ " " + operator + " ");//계산식을 추가해주기
        }
    }

    public void equalButtonclick(View view){// = 버튼에 연결. 태그 붙이기
        if(isFirstInput){
            if(isOperatorClick){//연산자라 클릭되면 처리하고 안되어 있으면 처리 하지 않음
                //숫자를 누르고 다른 연산 없이 =를 계속 누르면 연산이 되고 있음 연산자 눌렸는지 확인 변수 필요
                activityMainBinding.mathTextView.setText(resultNumber+ " " + lastOperator+ " " + inputNumber+ " =");//계산식 쓰기
                resultNumber=calculator(resultNumber, inputNumber, lastOperator);//아래에서  operator은 =으로 변경되어 있어어 lastOperator 사용
                activityMainBinding.resultTextView.setText(String.valueOf(resultNumber));//다시 계산 결과를 쓰기
            }
        }else {
            //double inputNumber=activityMainBinding.resultTextView.getText().toString();//문자를 double로 변환하지 못해서
            inputNumber=Double.parseDouble(activityMainBinding.resultTextView.getText().toString());//문자열을 더블형으로

            resultNumber=calculator(resultNumber,inputNumber,operator);//메소드를 만들어 사용

            //여기에서 말고 연산자 클릭에서 설정하기
            //=을 입력했을 때 마지막으로 사용된 연산자 -> 계속 =를 클릭했을 때 사용하기
            //lastOperator=operator;//마지막 연산자에 = 이전에 사용된 연산자를 넣어 기억하기

            activityMainBinding.resultTextView.setText(String.valueOf(resultNumber));//연산 결과 값
            isFirstInput=true;//다시 입력이 초기화
            operator=view.getTag().toString();//태그에 등록된 값을 연산자에 넣는다.
            activityMainBinding.mathTextView.append(inputNumber+ " " + operator + " ");//계산식을 추가해주기
        }
    }

    //백스페이스 버튼 클릭 %버튼에 연결
    public void backspaceButtonClick(View view) {
        if (!isFirstInput) {// ! 거짓이면
            String getResultText = activityMainBinding.resultTextView.getText().toString();
            if (getResultText.length() > 1) {
                String subString = getResultText.substring(0, getResultText.length() - 1);//뒤 한자리 빼기
                activityMainBinding.resultTextView.setText(subString);//한자리 뺀것을 다시 써 주기
            } else {
                activityMainBinding.resultTextView.setText("0");//1개까지 모두 없어지면 0으로
                isFirstInput = true;//참참            }
            }
        }
    }

        //전체를 전역 변수를 모두 사용하고 있는에 이것을 지역변수로 만들어 사용하고 반환값만 전역 변수로
        //private void calculator(double inputNumber) {
        private double calculator ( double resultNumber, double inputNumber, String operator ){
            //=가 있을 때의 처리
         /*
         if(operator.equals("=")){
             resultNumber=inputNumber;//결과값에 입력 값을 넣기
         }if (operator.equals("＋")) {
            resultNumber = resultNumber + inputNumber;
        } else if (operator.equals("－")) {
            resultNumber = resultNumber - inputNumber;

        } else if (operator.equals("×")) {
            resultNumber = resultNumber * inputNumber;

        } else if (operator.equals("÷")) {
            resultNumber = resultNumber / inputNumber;
        }
        */
            switch (operator) {
                case "=":
                    resultNumber = inputNumber;//결과값에 입력 값을 넣기
                    break;
                case "＋":
                    resultNumber = resultNumber + inputNumber;
                    break;
                case "－":
                    resultNumber = resultNumber - inputNumber;
                    break;
                case "×":
                    resultNumber = resultNumber * inputNumber;
                    break;
                case "÷":
                    resultNumber = resultNumber / inputNumber;
                    break;
                default:
                    Log.e("calculator", resultNumber + " " + inputNumber + " " + operator);
                    break;
            }
            return resultNumber;//반환 값이 없으면 에러남
    }
}

  /*
    public void numButtonClick(View view) {//이것을 버튼의 눌렸을 때 View에 정보가 들어가 있다.
        //Button button = findViewById(view.getId());//view에서 늘려진 해당 버튼의 개체 정보를 받아옴
        //String getButtonText = button.getText().toString();//해당 버튼의 text를 문자열로 받아 준비
        //tag를 구분자로 사용하기- tag 값을 다 넣어주고(위의 식을 아래 한 줄로
        //String getButtonText =view.getTag().toString();//해당 버튼의 tag값을 문자열로 받아 준비
        //아래의 변수에 바로 값을 넣어 주어도 된다.

        if (isFirstInput) {
            //activityMainBinding.resultTextView.setText(getButtonText);//resultTextView에 쓴다
            activityMainBinding.resultTextView.setText(view.getTag().toString());//resultTextView에 쓴다
            activityMainBinding.mathTextView.setText(view.getTag().toString());//mathTextView에 쓴다
            isFirstInput = false;
        } else {
            //activityMainBinding.resultTextView.append(getButtonText);//resultTextView에 추가된다.
            activityMainBinding.resultTextView.append(view.getTag().toString());//resultTextView에 추가된다.
            activityMainBinding.mathTextView.append(view.getTag().toString());//mathTextView에 추가된다.
        }
    }


    public void num1Click(View view) {//이것을 버튼의 onClick에서 등록하기
        if(isFirstInput){
            activityMainBinding.resultTextView.setText("1");//resultTextView에 숫자를 쓴다
            activityMainBinding.mathTextView.setText("1");//mathTextView에 숫자를 쓴다
            isFirstInput=false;
        }else {
            activityMainBinding.resultTextView.append("1");//resultTextView에 "1"이 추가된다.
            activityMainBinding.mathTextView.append("1");//mathTextView에 "1"이 추가된다.
        }
    }
    public void num2Click(View view) {//이것을 버튼의 메소드로 등록하기
        if(isFirstInput){
            activityMainBinding.resultTextView.setText("2");//resultTextView에 숫자를 쓴다
            activityMainBinding.mathTextView.setText("2");//mathTextView에 숫자를 쓴다
            isFirstInput=false;
        }else {
            activityMainBinding.resultTextView.append("2");//resultTextView에 "2"이 추가된다.
            activityMainBinding.mathTextView.append("2");//mathTextView에 "2"이 추가된다.
        }
    }
    */
