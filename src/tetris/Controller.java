import java.awt.Color;
import java.awt.Point;

import javax.swing.JOptionPane;

/*
 * Main과 다른 객체들 사이를 연결하는 객체
 */
public class Controller
{
	//Board의 정사각형 한 변의 길이
	public static final int		SQUARE_SIZE = 25;

	//drop()이 실행되는 주기(ms)
	public static int			TIME_GAP = 1000;

	/*
	 * 각 줄 제거시 얻는 점수의 양
	 * 1줄 : 100
	 * 2줄 : 300
	 * 3줄 : 600
	 * 4줄 : 1000
	 */

	public static final int[]	SCORE_PER_LINES_CLEARED = { 100, 300, 600, 1000 };

	//1조각 당 얻는 점수
	public static final int		SCORE_PER_PIECE = 10;

	//하드드롭 수행 시 보너스 점수
	public static final int		HARD_DROP_BONUS_SCORE = 5;

	//piece queue의 크기
	public static final int		PIECE_QUEUE_SIZE = 2;

	//board의 가로 칸 수
	public static final int		BOARD_WIDTH = 10;

	//bard의 세로 칸 수
	public static final int		BOARD_HEIGHT = 21;

	/*
	 * tetramino piece의 정보, TETRAMINO[shape][ratotion][porint]로 구성
	 * shape : tetramino piece의 종류, I / J / L / O / S / T / Z 형으로 나뉜다.
	 * rotation : tetramino piece의 회전, 0 / 90 / 180 / 270 도 회전이 가능 
	 * point : tetramino의 회전 상태에 따른 사각형의 좌표. 좌상단이 기준
	 */
	public static final Point[][][] TETRAMINOS = {
		{	{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) } },

		{	{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) } },

		{	{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) } },

		{	{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) } },

		{	{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
			{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) } },

		{	{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
			{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) } },

		{	{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
			{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) } } };
	
	//tetramino의 color 
	public static final Color[]	TETRAMINO_COLORS = {
		new Color(0, 216, 255),
		new Color(1, 0, 255),
		new Color(255, 187, 0),
		new Color(255, 228, 0),
		new Color(29, 219, 22),
		new Color(95, 0, 255),
		new Color(255, 0, 0) };


	//main 프레임의 sidePanel
	private SidePanel			sidePanel;


	//main 프레임의 tetrisPanel
	private TetrisPanel			tetrisPanel;

	//게임이 이루어지는 보드의 상황을 저장하는 객체
	public Color[][]			board;

	//현재 piece의 종류
	public int					shape;

	//현재 piece의 회전 상태
	public int					rotation;

	//현재 piece의 위피
	private Point				pieceLocation;

	//현재 게임의 득점 상황
	private int					score = 0;

	//다음 2개의 piece의 종류 정보
	private int[]				queue = new int[2];

	//현재 게임에서 제거한 줄 수
	private int					lines = 0;

	//게임의 진행중 여부
	private boolean				playing = false;

	//게임 진행 시간
	private int					timePassed = 0;

	/*
	 * timePassed를 반환함
	 */
	public int	getTimePassed()
	{
		return (timePassed);
	}

	/*
	 * timePassed에 인자 값을 대입함
	 */
	public void	setTimePassed(int timePassed)
	{
		this.timePassed = timePassed;
		return ;
	}

	/*
	 * queue의 index = i인 값을 반환
	 */
	public int	getQueue(int i)
	{
		return (queue[i]);
	}

	/*
	 * score 값을 반환함
	 */
	public int	getScore()
	{
		return (score);
	}

	/*
	 * score에 인자 값을 더함
	 */
	public void	addScore(int score)
	{
		this.score += score;
		return ;
	}

	/*
	 * playing 값을 반환함
	 */
	public boolean	isPlaying()
	{
		return (playing);
	}

	/*
	 * playing에 인자 값을 대입
	 */
	public void setPlaying(boolean playing)
	{
		this.playing = playing;
		return ;
	}

	/*
	 * lines 값을 반환함
	 */
	public int	getLines()
	{
		return (lines);
	}

	/*
	 * lines에 인자 값을 더함
	 */
	public void	addLines(int lines)
	{
		this.lines += lines;
		return ;
	}

	/*
	 * sidePanel 반환
	 */
	public SidePanel getSidePanel()
	{
		return (sidePanel);
	}

	/*
	 * sidePanel 저장
	 */
	public void	setSidePanel(SidePanel sidePanel)
	{
		this.sidePanel = sidePanel;
		return ;
	}

	/*
	 * tetrisPanel 반환
	 */
	public TetrisPanel	getTetrisPanel()
	{
		return (tetrisPanel);
	}

	/*
	 * tetrisPanel 저장
	 */
	public void	setTetrisPanel(TetrisPanel tetrisPanel)
	{
		this.tetrisPanel = tetrisPanel;
		return ;
	}

	/*
	 * pieceLocation 반환
	 */
	public Point	getPieceLocation()
	{
		return (pieceLocation);
	}

	/*
	 * priceLocation 저장
	 */
	public void	setPieceLoaction(Point pieceLocation)
	{
		this.pieceLocation = pieceLocation;
		return ;
	}

	/*
	 * 현재 piece의 값을 초기화함
	 * queue에 새로운 값을 추가함
	 * queue의 다음 piece 모양 값을 반환함
	 */
	public int	getNextPiece()
	{
		int nextpiece = queue[0];

		setPieceLoaction(new Point(5, 1));
		rotation = 0;
		queue[0] = queue[1];
		queue[1] = (int) (Math.random() * 7);
		return (nextpiece);
	}

	/*
	 * queue에 랜덤한 값을 저장함
	 */
	private void	setPieceQueue()
	{
		queue[0] = (int) (Math.random() * 7);
		queue[1] = (int) (Math.random() * 7);
		return ;
	}

	/*
	 * 게임을 시작하기 위해 필요한 초기화 작업을 실시함
	 * 이후 playing을 true로 전환
	 */
	public void initGame()
	{
		setPieceQueue();
		tetrisPanel.initBoard();
		sidePanel.setSidePanel();
		playing = true;
		new Thread(tetrisPanel).start();
		new Thread(sidePanel).start();
		return ;
	}

	/*
	 * sidePanel을 업데이트함
	 */
	public void updateSidePanel()
	{
		sidePanel.setSidePanel();
		return ;
	}

	/*
	 * 게임 종료 메소드
	 */
	public void finishGame()
	{
		playing = false;
		JOptionPane.showMessageDialog(null, "GAME OVER");
		JOptionPane.showMessageDialog(null, "SCORE : " + score + "\nTIME : " + timePassed + "s");
		System.exit(0);
	}

}
