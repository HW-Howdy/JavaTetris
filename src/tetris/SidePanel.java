import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class SidePanel extends JPanel implements Runnable
{
	private static final String	IMAGE_PATH = "nextpieces/";
	/*
	 * Controller 객체
	 */
	private Controller			controller;

	/*
	 * 게임 진행시간 출력을 위한 Panel
	 */
	private JPanel				timePanel = new JPanel();

	/*
	 * 다음 piece 정보를 add할 객체들
	 */
	private JPanel				nextPiecePanels = new JPanel();
	private JPanel				nextPiecePanel1 = new JPanel();
	private JPanel				nextPiecePanel2 = new JPanel();

	/*
	 * Controller 객체의 score와 lines 값을 출력하는 Panel
	 */
	private JPanel				scoreAndLinePanel = new JPanel();

	/*
	 * 게임 진행 시간을 출력하기 위한 Label
	 */
	private JLabel				time = new JLabel();

	/*
	 * 점수를 출력하기 위한 Label
	 */
	private JLabel				score = new JLabel();

	/*
	 * 제거한 줄 수를 출력하기 위한 Label
	 */
	private JLabel				lines = new JLabel();

	/*
	 * 객체의 생성 및 초기화, 하위 패널들의 생성 및 초기화
	 */
	public SidePanel(Controller controller)
	{
		this.controller = controller;
		this.setLayout(new BorderLayout(0, 10));
		setBorder(new EmptyBorder(26, 0, 26, 10));
		
		timePanel.setLayout(new GridLayout(2, 1));
		timePanel.setBorder(new LineBorder(Color.BLACK));
		timePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		timePanel.add(new Label("TIME"));
		timePanel.add(time);
		setLabelSetting(time);

		nextPiecePanels.setLayout(new BorderLayout());
		nextPiecePanels.setBorder(new LineBorder(Color.BLACK));
		nextPiecePanels.add(new Label("Next PIECE"), BorderLayout.NORTH);
		nextPiecePanels.setBorder(new EmptyBorder(10, 10, 10, 10));
		nextPiecePanels.add(nextPiecePanel1, BorderLayout.CENTER);
		nextPiecePanels.add(nextPiecePanel2, BorderLayout.SOUTH);
		
		scoreAndLinePanel.setLayout(new GridLayout(4, 1));
		scoreAndLinePanel.setBorder(new LineBorder(Color.BLACK));
		scoreAndLinePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		scoreAndLinePanel.add(new JLabel("SCORE"));
		scoreAndLinePanel.add(score);
		scoreAndLinePanel.add(new JLabel("LINES"));
		scoreAndLinePanel.add(lines);
		setLabelSetting(score);
		setLabelSetting(lines);

		this.add(timePanel, BorderLayout.NORTH);
		this.add(nextPiecePanels, BorderLayout.CENTER);
		this.add(scoreAndLinePanel, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(156, 0));
	}
	/*
	 * 표시되는 정보를 업데이트함
	 */
	public void	setSidePanel()
	{
		score.setText(Integer.toString(controller.getScore()));
		lines.setText(Integer.toString(controller.getLines()));
		paintNextPiece();
		return ;
	}

	/*
	 * nextPiecePanel을 초기화 후 업데이트함
	 */
	public void paintNextPiece()
	{
		ImageIcon	imageIcon1 = new ImageIcon(IMAGE_PATH + controller.getQueue(0) + ".jpg");
		JLabel		nextPiece1 = new JLabel(imageIcon1);
		ImageIcon	imageIcon2 = new ImageIcon(IMAGE_PATH + controller.getQueue(1) + ".jpg");
		JLabel		nextPiece2 = new JLabel(imageIcon2);

		nextPiecePanel1.removeAll();
		nextPiecePanel1.add(nextPiece1);
		nextPiecePanel1.revalidate();
		nextPiecePanel2.removeAll();
		nextPiecePanel2.add(nextPiece2);
		nextPiecePanel2.revalidate();
		return ;
	}

	/*
	 * Label의 폰트 및 정렬을 세팅함
	 */
	public void	setLabelSetting(JLabel label)
	{
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(new Font("SansSerif", Font.BOLD, 20));
		return ;
	}

	/*
	 * 게임 진행시간을 출력하는 메소드
	 */
	@Override
	public void run()
	{
		while (controller.isPlaying())
		{
			try
			{
				time.setText(Integer.toString(controller.getTimePassed()));
				Thread.sleep(1000);
				controller.setTimePassed(controller.getTimePassed() + 1);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return ;
	}

}
