import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/*
 * 메인 프레임
 */
public class Main extends JFrame 
{
	/*
	 * Main 생성자. Main 프레임을 생성 후 초기화 함
	 * 또한 하위 패널들을 add함
	 */
	public Main(SidePanel sidePanel, TetrisPanel tetrisPanel, TopPanel topPanel)
	{
		super("Tetris");
		setSize(494, 750);
		setLocation(100, 50);
		setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
		setLayout(new BorderLayout(10, 0));
		add(sidePanel, BorderLayout.EAST);
		add(tetrisPanel, BorderLayout.CENTER);
		add(topPanel, BorderLayout.NORTH);

		addKeyListener(new KeyListener()
		{
			@Override
			public void	keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e)
			{
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						tetrisPanel.rotate(+1);
						break ;
					case KeyEvent.VK_DOWN:
						tetrisPanel.drop();
						break ;
					case KeyEvent.VK_LEFT:
						tetrisPanel.move(-1);
						break ;
					case KeyEvent.VK_RIGHT:
						tetrisPanel.move(+1);
						break ;
					case KeyEvent.VK_SPACE:
						while (!tetrisPanel.isTouched(	tetrisPanel.getController().getPieceLocation().x,
														tetrisPanel.getController().getPieceLocation().y + 1,
														tetrisPanel.getController().rotation))
						{
							tetrisPanel.drop();
						}
						tetrisPanel.doOnWallTouched();
						tetrisPanel.getController().addScore(Controller.HARD_DROP_BONUS_SCORE);
						break;
					case KeyEvent.VK_SHIFT:
						tetrisPanel.hold();
						break ;
				}
			}
		});
	}

	/*
	 * Main 프레임 및 하위 패널들과 컨트롤러를 초기화함
	 * 또한 게임을 시작함
	 */
	public static void startGame()
	{
		Controller		controller = new Controller();
		SidePanel		sidePanel = new SidePanel(controller);
		TetrisPanel		tetrisPanel = new TetrisPanel(controller);
		TopPanel		topPanel = new TopPanel(controller);
		Main			main = new Main(sidePanel, tetrisPanel, topPanel);

		controller.setSidePanel(sidePanel);
		controller.setTetrisPanel(tetrisPanel);
		controller.setTopPanel(topPanel);
		controller.initGame();
		main.setVisible(true);
		return ;
	}

	public static void main(String[] args)
	{
		startGame();
		return ;
	}
	
}