export const bedStatusMap = {
  '1': '可用',
  '2': '已占用',
  '3': '维修中',
  '可用': '可用',
  '空闲': '可用',
  '已占用': '已占用',
  '已预约': '已占用',
  '已入住': '已占用',
  '维修中': '维修中'
}

export const reservationStatusMap = {
  '1': '待审核',
  '2': '已通过',
  '3': '已拒绝',
  '4': '已取消',
  '待审核': '待审核',
  '已通过': '已通过',
  '已拒绝': '已拒绝',
  '已取消': '已取消'
}

export function getBedStatusLabel(value) {
  const text = String(value ?? '').trim()
  return bedStatusMap[text] || '未知'
}

export function getBedStatusTagType(value) {
  const text = String(value ?? '').trim()
  switch (text) {
    case '1':
    case '可用':
    case '空闲':
      return 'success'
    case '2':
    case '已占用':
    case '已预约':
    case '已入住':
      return 'danger'
    case '3':
    case '维修中':
      return 'warning'
    default:
      return 'info'
  }
}

export function getReservationStatusLabel(value) {
  const text = String(value ?? '').trim()
  return reservationStatusMap[text] || '未知'
}

export function getReservationStatusTagType(value) {
  const text = String(value ?? '').trim()
  switch (text) {
    case '1':
    case '待审核':
      return 'warning'
    case '2':
    case '已通过':
      return 'success'
    case '3':
    case '已拒绝':
      return 'danger'
    case '4':
    case '已取消':
      return 'info'
    default:
      return 'info'
  }
}
