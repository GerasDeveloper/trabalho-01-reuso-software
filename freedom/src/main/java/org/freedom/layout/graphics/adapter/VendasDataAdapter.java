package org.freedom.layout.graphics.adapter;

import java.sql.SQLException;
import java.util.List;

/**
 * Padrão GoF Adapter (Estrutural).
 * Interface Target que define o contrato de fornecimento de dados de vendas
 * aos relatórios gráficos, eliminando o acoplamento direto com java.sql.ResultSet.
 */
public interface VendasDataAdapter {

	List<RegistroVenda> carregarRegistros() throws SQLException;
}
