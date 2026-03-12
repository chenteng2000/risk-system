const XLSX = require('xlsx');
const path = require('path');

// 创建工作簿
const wb = XLSX.utils.book_new();
const ws = XLSX.utils.aoa_to_sheet([
  ['projectName', 'projectCode', 'stage', 'investmentAmount'],
  ['示例项目', 'PRJ001', '天使轮', 500]
]);

// 设置列宽
ws['!cols'] = [
  { wch: 20 },  // projectName
  { wch: 20 },  // projectCode
  { wch: 15 },  // stage
  { wch: 18 }   // investmentAmount
];

// 添加工作表到工作簿
XLSX.utils.book_append_sheet(wb, ws, '项目导入');

// 保存文件
const filePath = path.join(__dirname, 'risk-system/src/main/resources/模版.xlsx');
XLSX.writeFile(wb, filePath);
console.log('✓ Excel 模板已生成：' + filePath);
