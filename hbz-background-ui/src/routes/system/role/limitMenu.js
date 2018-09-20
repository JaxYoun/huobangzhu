import React from 'react'
import { Select } from 'antd'
import PropTypes from 'prop-types'

const Option = Select.Option

const EditableCell = ({
  onChange,
  record,
}) => {
  const maxLevel = 99
  const level = record.level || maxLevel
  const limitOption = [
    '请选择...',
    '[创建] [更新] [执行] [删除]',
    '[创建] [更新] [执行]',
    '[创建] [更新]',
    '[创建] [执行]',
    '[创建]',
    '[执行]',
  ]
  const options = limitOption.map((item, index) => {
    const i = index === 0 ? maxLevel : index
    return <Option value={i}>{item}</Option>
  })

  const onchangeMenu = (levelId) => {
    record.level = levelId
    onChange(record)
  }
  return (
    <div className="editable-cell">
      <Select
        value={level}
        onChange={onchangeMenu}
      >
        {options}
      </Select>
    </div>
  )
}
EditableCell.propTypes = {
  value: PropTypes.string,
  onChange: PropTypes.func,
  record: PropTypes.object,
}
export default EditableCell
