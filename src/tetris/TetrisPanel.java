import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

public class TetrisPanel extends JPanel implements Runnable
{
	/*
	 * controller 객체
	 */
	private Controller	controller;

	/*
	 * 생성자
	 */
	public TetrisPanel(Controller controller)
	{
		super();
		this.controller = controller;
	}

	/*
	 * controller를 반환함
	 */
	public Controller getController()
	{
		return (controller);
	}

	/*
	 * board 초기화 및 piece 설정
	 */
	public void	initBoard()
	{
		controller.board = new Color[Controller.BOARD_WIDTH + 2][Controller.BOARD_HEIGHT + 3];
		for (int i = 0; i < Controller.BOARD_WIDTH + 2; i++)
		{
			for (int j = 0; j < Controller.BOARD_HEIGHT + 3; j++)
			{
				if (i == 0 || i == Controller.BOARD_WIDTH + 1 || j == 0 || j == Controller.BOARD_HEIGHT + 2)
					controller.board[i][j] = Color.GRAY;
				else
					controller.board[i][j] = Color.BLACK;
			}
		}
		controller.shape = controller.getNextPiece();
		return ;
	}

	/*
	 * 현재 piece를 board에 출력
	 */
	private void	drawPiece(Graphics g)
	{
		g.setColor(Controller.TETRAMINO_COLORS[controller.shape]);
		for (Point p : Controller.TETRAMINOS[controller.shape][controller.rotation])
		{
			g.fillRect(	(p.x + controller.getPieceLocation().x) * (Controller.SQUARE_SIZE + 1),
						(p.y + controller.getPieceLocation().y) * (Controller.SQUARE_SIZE + 1),
						Controller.SQUARE_SIZE,
						Controller.SQUARE_SIZE);
		}
		return ;
	}

	/*
	 * board 업데이트를 위해 paintComponent(Graphics g) 오버라이드
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		g.fillRect(0, 0, (Controller.SQUARE_SIZE + 1) * 12, (Controller.SQUARE_SIZE + 1) * 24);
		for (int i = 0; i < Controller.BOARD_WIDTH + 2; i++)
		{
			for (int j = 0; j < Controller.BOARD_HEIGHT + 3; j++)
			{
				g.setColor(controller.board[i][j]);
				g.fillRect(	(Controller.SQUARE_SIZE + 1) * i,
							(Controller.SQUARE_SIZE + 1) * j,
							Controller.SQUARE_SIZE,
							Controller.SQUARE_SIZE);
			}
		}
		drawPiece(g);
		return ;
	}

	/*
	 * piece가 벽과 닿는지 여부를 판별함
	 */
	public boolean	isTouched(int x, int y, int rotation)
	{
		boolean	result = false;
		for (Point p : Controller.TETRAMINOS[controller.shape][rotation])
		{
			if (controller.board[p.x + x][p.y + y] != Color.BLACK)
			{
				result = true;
				break ;
			}
		}
		return (result);
	}

	/*
	 * 현재 piece의 회전상태를 i만큼 변경함
	 */
	public void rotate(int i)
	{
		int	newRotation = (controller.rotation + i) % 4;
		if (newRotation < 0)
			newRotation += 3;
		if (!isTouched(controller.getPieceLocation().x, controller.getPieceLocation().y, newRotation))
			controller.rotation = newRotation;
		getParent().repaint();
		return ;
	}

	/*
	 * 현재 piece의 x 좌표를 i만큼 변경함
	 */
	public void	move(int i)
	{
		if (!isTouched(controller.getPieceLocation().x + i, controller.getPieceLocation().y, controller.rotation))
			controller.getPieceLocation().x += i;
		getParent().repaint();
		return ;
	}

	/*
	 * 현재 piece의 y 좌표를 +1만큼 변경함
	 * 벽과 맞닿으면 doOnWallTouched() 호출
	 */
	public void	drop()
	{
		if (!isTouched(controller.getPieceLocation().x, controller.getPieceLocation().y + 1, controller.rotation))
			controller.getPieceLocation().y += 1;
		else
			doOnWallTouched();
		getParent().repaint();
		return ;
	}

	/*
	 * 줄을 제거하는 메소드
	 */
	public void	deleteRow(int row)
	{
		for (int j = row - 1; j > 0; j--)
		{
			for (int i = 0; i < Controller.BOARD_WIDTH + 1; i++)
			{
				controller.board[i][j + 1] = controller.board[i][j];
			}
		}
		return ;
	}

	/*
	 * 모든 줄을 체크하여 해당하는 줄이 꽉 찼으면 그 줄을 제거함
	 * 제거한 줄 수에 따라 score를 추가함
	 */
	public void clearRows()
	{
		boolean	gap;
		int		clearNo = 0;

		for (int j = Controller.BOARD_HEIGHT; j > 0; j--)
		{
			gap = false;
			for (int i = 1; i < Controller.BOARD_WIDTH + 1; i++)
			{
				if (controller.board[i][j] == Color.BLACK)
				{
					gap = true;
					break ;
				}
			}
			if (!gap)
			{
				deleteRow(j);
				j += 1;
				clearNo += 1;
			}
		}
		if (clearNo > 0)
		{
			controller.addLines(clearNo);
			controller.addScore(Controller.SCORE_PER_LINES_CLEARED[clearNo - 1]);
		}
		return ;
	}

	/*
	 * 피스가 벽에 닿으면 호출됨
	 * 피스의 y좌표가 1 이하라면 게임을 멈춤
	 * 
	 */
	public void	doOnWallTouched()
	{
		for (Point p : Controller.TETRAMINOS[controller.shape][controller.rotation])
		{
			controller.board[controller.getPieceLocation().x + p.x][controller.getPieceLocation().y + p.y] = Controller.TETRAMINO_COLORS[controller.shape];
		}
		if (controller.getPieceLocation().y < 2)
		{
			controller.setPlaying(false);
			controller.finishGame();
		}
		else
		{
			controller.addScore(Controller.SCORE_PER_PIECE);
			clearRows();
			controller.shape = controller.getNextPiece();
			controller.updateSidePanel();
		}
		return ;
	}

	/*
	 * 주어진 시간마다 drop()을 호출하여 게임을 진행시킴
	 */
	@Override
	public void run()
	{
		while (controller.isPlaying())
		{
			try
			{
				Thread.sleep(Controller.TIME_GAP);
				if (Controller.TIME_GAP >= 100)
				{
					Controller.TIME_GAP -= 5;
				}
				drop();
			}
			catch (InterruptedException e)
			{

			}
		}
		return ;
	}
}
