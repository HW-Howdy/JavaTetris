import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Label;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

class TopPanel extends JPanel 
{
	private static final String	IMAGE_PATH = "./../../nextpieces/";

	/*
	 * Main 프레임의 controller 객체
	 */
	private	Controller	controller;

	/*
	 * hold한 piece를 출력하는 panels
	 */
	private JPanel		holdPiecePanel = new JPanel();
	private JPanel		holdPieceImagePanel = new JPanel();

	/*
	 * 객체의 생성 및 초기화, 하위 패널들의 생성 및 초기화
	 */
	public TopPanel(Controller controller)
	{
		this.controller = controller;
		this.setLayout(new BorderLayout(0, 30));

		holdPiecePanel.setLayout(new BorderLayout());
		holdPiecePanel.add(new Label("HOLD PIECE"), BorderLayout.WEST);
		holdPiecePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		holdPiecePanel.add(holdPieceImagePanel, BorderLayout.EAST);

		this.add(holdPiecePanel, BorderLayout.WEST);
		//setPreferredSize(new Dimension(0, 0));
	}
	
	/*
	 * 표시되는 정보 초기화
	 */
	public void	initTopPanel()
	{
		holdPieceImagePanel.removeAll();
		holdPieceImagePanel.add(new JLabel());
		holdPieceImagePanel.setBorder(new EmptyBorder(25, 0, 25, 0));
		holdPieceImagePanel.revalidate();
		return ;
	}

	/*
	 * 표시되는 정보를 업데이트함
	 */
	public void	setTopPanel()
	{
		ImageIcon	imageIcon = new ImageIcon((new ImageIcon(IMAGE_PATH + controller.getHoldPiece() + ".jpg")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		JLabel		holdPiece = new JLabel(imageIcon);

		holdPieceImagePanel.removeAll();
		holdPieceImagePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		holdPieceImagePanel.add(holdPiece);
		holdPieceImagePanel.revalidate();
		return ;
	}
}