import { GridOptions } from 'ag-grid-community';
import { Column } from 'ag-grid-community/dist/lib/entities/column';

export function onGridSizeChanged(params: GridOptions): void {
  if (!document.getElementById('grid-wrapper')) {
    return;
  }
  const gridWidth = document.getElementById('grid-wrapper')!.offsetWidth;
  let columnsToShow: string[] = [];
  let columnsToHide: string[] = [];
  let totalColsWidth = 0;
  const allColumns: Column[] = params.columnApi!.getAllColumns();
  allColumns.forEach((column) => {
    totalColsWidth += column.getMinWidth();
    if (totalColsWidth > gridWidth) {
      columnsToHide = [...columnsToHide, column.getColId()];
    } else {
      columnsToShow = [...columnsToShow, column.getColId()];
    }
  });
  params.columnApi!.setColumnsVisible(columnsToShow, true);
  params.columnApi!.setColumnsVisible(columnsToHide, false);
  params.api!.sizeColumnsToFit();
}
