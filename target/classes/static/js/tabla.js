/**
 * Script para gestionar tablas con ordenamiento, selección múltiple y paginación
 */

// Estado global
let sortState = {};
let paginationState = {};
let filterState = {};

const ITEMS_PER_PAGE_OPTIONS = [10, 25, 50, 100, 150, 200];
const DEFAULT_ITEMS_PER_PAGE = 10;

/**
 * Inicializa la funcionalidad de tablas con paginación
 * @param {string} tableId - ID de la tabla
 */
function initTable(tableId) {
    const table = document.getElementById(tableId);
    if (!table) return;

    // Inicializar estado de filtrado
    filterState[tableId] = {};

    // Inicializar estado de paginación
    paginationState[tableId] = {
        currentPage: 1,
        itemsPerPage: DEFAULT_ITEMS_PER_PAGE,
        totalRows: table.querySelectorAll('tbody tr').length
    };

    // Agregar event listeners a los encabezados para ordenamiento
    const headers = table.querySelectorAll('thead th[data-column]');
    headers.forEach(header => {
        header.style.cursor = 'pointer';
        header.addEventListener('click', function(e) {
            // No ordenar si se hace clic en el input de filtro
            if (e.target.tagName === 'INPUT') return;
            
            const column = this.getAttribute('data-column');
            sortTable(tableId, column);
            updateSortIndicators(table, column);
            paginationState[tableId].currentPage = 1;
            updateTableDisplay(tableId);
        });
    });

    // Crear controles de filtrado
    createFilterControls(tableId);

    // Agregar event listener al checkbox "Seleccionar Todo"
    const selectAllCheckbox = document.getElementById(tableId + '-selectAll');
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', function() {
            selectAllRowsCurrentPage(tableId, this.checked);
        });
    }

    // Crear controles de paginación
    createPaginationControls(tableId);

    // Mostrar tabla paginada
    updateTableDisplay(tableId);
}

/**
 * Crea los controles de paginación
 * @param {string} tableId - ID de la tabla
 */
function createPaginationControls(tableId) {
    const table = document.getElementById(tableId);
    if (!table) return;

    // Buscar o crear contenedor de paginación
    let paginationContainer = document.getElementById(tableId + '-pagination');
    if (!paginationContainer) {
        paginationContainer = document.createElement('div');
        paginationContainer.id = tableId + '-pagination';
        paginationContainer.className = 'pagination-controls mt-4';
        table.parentElement.appendChild(paginationContainer);
    }

    renderPaginationControls(tableId);
}

/**
 * Renderiza los controles de paginación
 * @param {string} tableId - ID de la tabla
 */
function renderPaginationControls(tableId) {
    const container = document.getElementById(tableId + '-pagination');
    const state = paginationState[tableId];
    const totalPages = Math.ceil(state.totalRows / state.itemsPerPage);

    let html = `
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-3">
            <div class="items-per-page">
                <label for="${tableId}-itemsPerPage" class="me-2">Registros por página:</label>
                <select id="${tableId}-itemsPerPage" class="form-select form-select-sm" style="width: auto; display: inline-block;" onchange="changeItemsPerPage('${tableId}', this.value)">
    `;

    ITEMS_PER_PAGE_OPTIONS.forEach(option => {
        html += `<option value="${option}" ${option === state.itemsPerPage ? 'selected' : ''}>${option}</option>`;
    });

    html += `
                </select>
            </div>

            <div class="pagination-info">
                Mostrando ${Math.min((state.currentPage - 1) * state.itemsPerPage + 1, state.totalRows)} - ${Math.min(state.currentPage * state.itemsPerPage, state.totalRows)} de ${state.totalRows} registros
            </div>

            <nav aria-label="Paginación de tabla">
                <ul class="pagination pagination-sm mb-0">
                    <li class="page-item ${state.currentPage === 1 ? 'disabled' : ''}">
                        <a class="page-link" href="javascript:void(0)" onclick="goToPage('${tableId}', 1)">Primera</a>
                    </li>
                    <li class="page-item ${state.currentPage === 1 ? 'disabled' : ''}">
                        <a class="page-link" href="javascript:void(0)" onclick="goToPage('${tableId}', ${state.currentPage - 1})">Anterior</a>
                    </li>
    `;

    // Mostrar números de página
    const maxPagesToShow = 5;
    let startPage = Math.max(1, state.currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = Math.min(totalPages, startPage + maxPagesToShow - 1);
    startPage = Math.max(1, endPage - maxPagesToShow + 1);

    if (startPage > 1) {
        html += `<li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="goToPage('${tableId}', 1)">1</a></li>`;
        if (startPage > 2) {
            html += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
        }
    }

    for (let i = startPage;i <= endPage;i++) {
        html += `
            <li class="page-item ${i === state.currentPage ? 'active' : ''}">
                <a class="page-link" href="javascript:void(0)" onclick="goToPage('${tableId}', ${i})">${i}</a>
            </li>
        `;
    }

    if (endPage < totalPages) {
        if (endPage < totalPages - 1) {
            html += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
        }
        html += `<li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="goToPage('${tableId}', ${totalPages})">${totalPages}</a></li>`;
    }

    html += `
                    <li class="page-item ${state.currentPage === totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="javascript:void(0)" onclick="goToPage('${tableId}', ${state.currentPage + 1})">Siguiente</a>
                    </li>
                    <li class="page-item ${state.currentPage === totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="javascript:void(0)" onclick="goToPage('${tableId}', ${totalPages})">Última</a>
                    </li>
                </ul>
            </nav>
        </div>
    `;

    container.innerHTML = html;
}

/**
 * Actualiza la visualización de la tabla con paginación y filtrado
 * @param {string} tableId - ID de la tabla
 */
function updateTableDisplay(tableId) {
    const table = document.getElementById(tableId);
    const tbody = table.querySelector('tbody');
    const allRows = Array.from(tbody.querySelectorAll('tr'));
    const state = paginationState[tableId];
    const filters = filterState[tableId];

    // Filtrar filas
    const filteredRows = allRows.filter(row => {
        return matchesAllFilters(row, filters);
    });

    // Actualizar totalRows filtradas
    state.totalRows = filteredRows.length;

    // Calcular índices
    const startIndex = (state.currentPage - 1) * state.itemsPerPage;
    const endIndex = startIndex + state.itemsPerPage;

    // Ocultar/mostrar filas
    allRows.forEach((row) => {
        const isFiltered = filteredRows.includes(row);
        const indexInFiltered = filteredRows.indexOf(row);
        
        if (isFiltered && indexInFiltered >= startIndex && indexInFiltered < endIndex) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });

    // Actualizar controles de paginación
    renderPaginationControls(tableId);

    // Actualizar eventos de checkboxes en la página actual
    attachCheckboxListeners(tableId);
}

/**
 * Adjunta event listeners a los checkboxes de la página actual
 * @param {string} tableId - ID de la tabla
 */
function attachCheckboxListeners(tableId) {
    const table = document.getElementById(tableId);
    const rowCheckboxes = table.querySelectorAll('tbody input[type="checkbox"]');
    rowCheckboxes.forEach(checkbox => {
        checkbox.removeEventListener('change', handleCheckboxChange);
        checkbox.addEventListener('change', handleCheckboxChange);
    });
}

/**
 * Manejador genérico de cambio de checkbox
 */
function handleCheckboxChange(e) {
    const checkbox = e.target;
    const row = checkbox.closest('tr');
    const table = row.closest('table');
    const tableId = table.id;
    updateSelectAllCheckbox(tableId);
}

/**
 * Cambia la cantidad de registros por página
 * @param {string} tableId - ID de la tabla
 * @param {number} itemsPerPage - Nueva cantidad de registros
 */
function changeItemsPerPage(tableId, itemsPerPage) {
    paginationState[tableId].itemsPerPage = parseInt(itemsPerPage);
    paginationState[tableId].currentPage = 1;
    updateTableDisplay(tableId);
}

/**
 * Navega a una página específica
 * @param {string} tableId - ID de la tabla
 * @param {number} pageNumber - Número de página
 */
function goToPage(tableId, pageNumber) {
    const table = document.getElementById(tableId);
    const state = paginationState[tableId];
    const totalPages = Math.ceil(state.totalRows / state.itemsPerPage);

    if (pageNumber >= 1 && pageNumber <= totalPages) {
        state.currentPage = pageNumber;
        updateTableDisplay(tableId);

        // Scroll a la tabla
        table.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
}

/**
 * Ordena la tabla por una columna específica
 * @param {string} tableId - ID de la tabla
 * @param {string} column - Nombre de la columna a ordenar
 */
function sortTable(tableId, column) {
    const table = document.getElementById(tableId);
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.querySelectorAll('tr'));

    const currentSort = sortState[tableId] || {};
    const isAscending = currentSort.column === column ? !currentSort.ascending : true;

    rows.sort((a, b) => {
        const cellA = a.querySelector(`td[data-column="${column}"]`);
        const cellB = b.querySelector(`td[data-column="${column}"]`);

        if (!cellA || !cellB) return 0;

        const valueA = cellA.textContent.trim();
        const valueB = cellB.textContent.trim();

        const numA = Number.parseFloat(valueA);
        const numB = Number.parseFloat(valueB);

        if (!containsLetter(valueA) && !containsLetter(valueB)) {
            if (!isNaN(numA) && !isNaN(numB)) {
                return isAscending ? numA - numB : numB - numA;
            }
        }

        return isAscending ?
            valueA.localeCompare(valueB, 'es') :
            valueB.localeCompare(valueA, 'es');
    });

    rows.forEach(row => tbody.appendChild(row));
    sortState[tableId] = { column, ascending: isAscending };
}

function containsLetter(str) {
    return /[a-zA-Z\-\/]/.test(str);
}

/**
 * Crea los controles de filtrado (fila de filtros en el encabezado)
 * @param {string} tableId - ID de la tabla
 */
function createFilterControls(tableId) {
    const table = document.getElementById(tableId);
    if (!table) return;

    const thead = table.querySelector('thead');
    let filterRow = document.getElementById(tableId + '-filter-row');
    
    if (filterRow) {
        filterRow.remove();
    }

    filterRow = document.createElement('tr');
    filterRow.id = tableId + '-filter-row';
    filterRow.className = 'filter-row';
	const emptyTh = document.createElement('th');
	filterRow.appendChild(emptyTh);
	
    const headers = thead.querySelectorAll('th[data-column]');
    headers.forEach(header => {
        const column = header.getAttribute('data-column');
        const th = document.createElement('th');
        th.className = 'filter-header';
        
        const input = document.createElement('input');
        input.type = 'text';
        input.className = 'form-control form-control-sm filter-input';
        input.placeholder = `Filtrar ${header.textContent.trim()}`;
        input.setAttribute('data-column', column);
        
        input.addEventListener('input', function(e) {
            filterState[tableId][column] = e.target.value.toLowerCase();
            paginationState[tableId].currentPage = 1;
            updateTableDisplay(tableId);
        });

        th.appendChild(input);
        filterRow.appendChild(th);
    });

    // Agregar columna vacía para las acciones
    const actionTh = document.createElement('th');
    actionTh.className = 'filter-header';
    filterRow.appendChild(actionTh);

    thead.appendChild(filterRow);
}

/**
 * Verifica si una fila coincide con todos los filtros aplicados
 * @param {HTMLElement} row - Fila de la tabla
 * @param {Object} filters - Objeto con los valores de filtros
 * @returns {boolean} true si la fila coincide con todos los filtros
 */
function matchesAllFilters(row, filters) {
    for (const [column, filterValue] of Object.entries(filters)) {
        if (!filterValue || filterValue.trim() === '') continue;

        const cell = row.querySelector(`td[data-column="${column}"]`);
        if (!cell) continue;

        const cellText = cell.textContent.trim().toLowerCase();
        
        if (!cellText.includes(filterValue)) {
            return false;
        }
    }
    return true;
}

/**
 * Actualiza los indicadores visuales de ordenamiento
 * @param {HTMLElement} table - Elemento de tabla
 * @param {string} column - Nombre de la columna ordenada
 */
function updateSortIndicators(table, column) {
    const headers = table.querySelectorAll('thead th[data-column]');
    headers.forEach(header => {
        header.classList.remove('sort-asc', 'sort-desc');
        if (header.getAttribute('data-column') === column) {
            const isAscending = (sortState[table.id] || {}).ascending;
            header.classList.add(isAscending ? 'sort-asc' : 'sort-desc');
        }
    });
}

/**
 * Selecciona o deselecciona todas las filas de la página actual
 * @param {string} tableId - ID de la tabla
 * @param {boolean} checked - Estado del checkbox
 */
function selectAllRowsCurrentPage(tableId, checked) {
    const table = document.getElementById(tableId);
    const visibleCheckboxes = table.querySelectorAll('tbody tr:not([style*="display: none"]) input[type="checkbox"]');
    visibleCheckboxes.forEach(checkbox => {
        checkbox.checked = checked;
    });
}

/**
 * Actualiza el estado del checkbox "Seleccionar Todo"
 * @param {string} tableId - ID de la tabla
 */
function updateSelectAllCheckbox(tableId) {
    const table = document.getElementById(tableId);
    const visibleCheckboxes = table.querySelectorAll('tbody tr:not([style*="display: none"]) input[type="checkbox"]');
    const selectAllCheckbox = document.getElementById(tableId + '-selectAll');

    if (!selectAllCheckbox) return;

    const allChecked = Array.from(visibleCheckboxes).every(cb => cb.checked);
    const anyChecked = Array.from(visibleCheckboxes).some(cb => cb.checked);

    selectAllCheckbox.checked = allChecked;
    selectAllCheckbox.indeterminate = anyChecked && !allChecked;
}

/**
 * Obtiene los IDs seleccionados (todas las páginas)
 * @param {string} tableId - ID de la tabla
 * @returns {number[]} Array de IDs seleccionados
 */
function getSelectedIds(tableId) {
    const table = document.getElementById(tableId);
    const checkboxes = table.querySelectorAll('tbody input[type="checkbox"]:checked');
    return Array.from(checkboxes).map(checkbox => parseInt(checkbox.value));
}

/**
 * Elimina registros seleccionados
 * @param {string} tableId - ID de la tabla
 * @param {string} endpoint - Endpoint para eliminar registros
 * @param {string} entityName - Nombre de la entidad (para el mensaje)
 */
function deleteSelected(tableId, endpoint, entityName) {
    const selectedIds = getSelectedIds(tableId);

    if (selectedIds.length === 0) {
        alert('Por favor selecciona al menos un registro para eliminar');
        return;
    }

    const message = selectedIds.length === 1
        ? `¿Estás seguro de que deseas eliminar este ${entityName}?`
        : `¿Estás seguro de que deseas eliminar estos ${selectedIds.length} ${entityName}s?`;

    if (!confirm(message)) {
        return;
    }

    const form = document.createElement('form');
    form.method = 'POST';
    form.action = endpoint;

    selectedIds.forEach(id => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'ids';
        input.value = id;
        form.appendChild(input);
    });

    document.body.appendChild(form);
    form.submit();
}