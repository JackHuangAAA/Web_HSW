export default [
  {
    key: 'ID',
    width: 1
  },
  {
    key: 'type',
    title: '报警类型',
    width: 3,
    static: true,
    map: 'alarminfo'
  },
  {
    key: 'createDate',
    title: '报警时间',
    width: 4,
    type: 'datetime'
  },
  { key: 'reason', title: '报警原因', width: 8 },
  {
    key: 'solution',
    title: '解决情况',
    width: 8
  }
];
