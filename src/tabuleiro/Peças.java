package tabuleiro;

public class Pe�as {
	
	protected Posi��es posicao;
	private tabuleiro tabuleiro;
	public Pe�as(tabuleiro tabuleiro) {
			this.tabuleiro = tabuleiro;
			posicao=null;
	}
	protected tabuleiro getTabuleiro() {
		return tabuleiro;
	}


}
