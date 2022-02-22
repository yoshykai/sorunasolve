import mymatome.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Main {
	static int size=12;
	static String resstr = "";
	static HashMap<String,Boolean> map;
	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.add(new Draw());
    app.setSize(800,400);
    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    app.setVisible(true);
	}
}

class Draw extends JPanel{
	JButton culc,reset,move;
  JTextField textF[][];
	JTextField ans,anumber,bnumber;
	JComboBox<String> startCombo,endCombo;
	int nums,numk;
	String ban[];
	HashMap<String,Boolean> map;
	int resnum[];
	public Draw(){
		setLayout(null);
		nums=12;numk=4;
		ban=new String[]{"太陽","月","流星","星々"};
		textF = new JTextField[numk][nums];
		for(int i=0;i<numk;i++){
			for(int j=0;j<nums;j++){
				textF[i][j] = new JTextField("0"); textF[i][j].setBounds(60+j*60,40+i*40,50,30);
				add(textF[i][j]);
			}
		}
		ans = new JTextField(); ans.setBounds(5,240,200,30);
		ans.setEditable(false); add(ans);
		culc = new JButton("culc"); culc.setBounds(5,200,70,30); add(culc);
		reset = new JButton("reset"); reset.setBounds(80,200,70,30); add(reset);
		move = new JButton("move"); move.setBounds(155,200,70,30); add(move);
		startCombo = new JComboBox<String>(ban); startCombo.setBounds(220,240,80,30); add(startCombo);
		anumber = new JTextField(); anumber.setBounds(300,240,30,30); add(anumber);
		endCombo = new JComboBox<String>(ban); endCombo.setBounds(330,240,80,30); add(endCombo);
		bnumber = new JTextField(); bnumber.setBounds(410,240,30,30); add(bnumber);

		reset.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt){
				for(int i=0;i<numk;i++){
					for(int j=0;j<nums;j++){
						textF[i][j].setText("0");
					}
				}
			}
		});

		move.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt){
				if(!Trans.isNumber(anumber.getText())||!Trans.isNumber(bnumber.getText())){
					ans.setText("Error : 数字を入力してください");
					return;
				}
				int as = startCombo.getSelectedIndex();
				int anum = Trans.stoi(anumber.getText());
				int bs = endCombo.getSelectedIndex();
				int bnum = Trans.stoi(bnumber.getText());
				if(anum<=0||nums<anum||bnum<=0||nums<bnum||anum+bnum>num){
					ans.setText("Error : 無効な数字です");
					return;
				}
				int a = Trans.stoi(textF[as][anum-1].getText());
				int b = Trans.stoi(textF[bs][bnum-1].getText());
				if(a<=0||b<=0||(as==bs&&anum==bnum&&a<2)){
					ans.setText("Error : 無効な数字です");
					return;
				}
				textF[as][anum-1].setText(a-1+"");
				textF[bs][bnum-1].setText(b-1+"");
				if(as==bs&&anum==bnum){
					textF[bs][bnum-1].setText(b-2+"");
				}
				a = Trans.stoi(textF[as][anum+bnum-1].getText());
				textF[as][anum+bnum-1].setText((a+1)+"");
			}
		});

		culc.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt){
				int list[][] = new int[numk][nums+1];
				int count = 0;
				for(int i=0;i<numk;i++){
					for(int j=0;j<nums;j++){
						if(!Trans.isNumber(textF[i][j].getText())){
							ans.setText("Error : 数字を入力してください");
							return;
						}
						list[i][j+1]=Trans.stoi(textF[i][j].getText());
						count += list[i][j+1]*(j+1);
					}
				}
				if(count!=12){
					ans.setText("Error : 12になるようにしてください");
					return;
				}
				map = new HashMap<String,Boolean>();
				resnum = new int[4];
				boolean flg = func(list,true);
				if(flg){

					ans.setText(flg+" "+ban[resnum[0]]+","+resnum[1]+" -> " + ban[resnum[2]]+","+resnum[3]);
				}else{
					ans.setText(flg+"");
				}
      }
    });
	}

	public boolean func(int list[][],boolean turn){
		boolean res=true;
		String temp="",tstr=a2s(list);
		int dFlg=0;
		for(int i=0;i<numk;i++){
			for(int j=1;j<nums;j++){
				if(list[i][j]==0){continue;}
				for(int k=j+1;k<nums;k++){
					if(list[i][k]>0){
						dFlg = func(list,turn,i,j,i,k,tstr);
						if(dFlg==1){
							return true;
						}else if(dFlg==-1){
							return false;
						}
					}
				}
				for(int k=i;k<numk;k++){
					if(i==k && list[i][j]>=2){
						dFlg = func(list,turn,i,j,i,j,tstr);
						if(dFlg==1){
							return true;
						}else if(dFlg==-1){
							return false;
						}
					}else if(i!=k && list[k][j]>=1){
						dFlg = func(list,turn,i,j,k,j,tstr);
						if(dFlg==1){
							return true;
						}else if(dFlg==-1){
							return false;
						}

						dFlg = func(list,turn,k,j,i,j,tstr);
						if(dFlg==1){
							return true;
						}else if(dFlg==-1){
							return false;
						}
					}
				}
			}
		}
		map.put(tstr,!turn);
		return !turn;
	}

	private int func(int list[][],boolean turn,int as,int an,int bs,int bn,String str){
		boolean res = true;
		list[as][an]--;list[bs][bn]--;list[as][an+bn]++;
		String temp=a2s(list);
		if(map.containsKey(temp)){
			res = map.get(temp);
		}else{
			res = func(list,!turn);
		}
		list[as][an]++;list[bs][bn]++;list[as][an+bn]--;
		if(res&&turn){
			resnum[0]=as;resnum[1]=an;resnum[2]=bs;resnum[3]=bn;
			map.put(str,true);
			return 1;
		}else if(!res&&!turn){
			map.put(str,false);
			return -1;
		}
		return 0;
	}

	public String a2s(int list[][]){
		String str = "";
		for(int i=0;i<numk;i++){
			for(int j=0;j<=nums;j++){
				str+=t2s(list[i][j]);
			}
		}
		return str;
	}

	public String t2s(int a){
		if(a<10){
			return String.valueOf(a);
		}else{
			return String.valueOf((char)('A'+(a-10)));
		}
	}

	public void paintComponent(Graphics g){
		for(int i=0;i<numk;i++){
			g.drawString(ban[i],10,60+i*40);
		}
		for(int j=0;j<nums;j++){
			g.drawString((j+1)+"",80+j*60,30);
		}
  }
}
