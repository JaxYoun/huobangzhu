import React from 'react'
import { Modal, Form, Input, Select, InputNumber } from 'antd'
import PropTypes from 'prop-types'

const CarLengthModal = ({
  visible,
  transSizeData,
  dispatch,
  hideModal,
  selectCarsId,
  selectCarsName,
}) => {
  const selectCars = (data) => {
    selectCarsId.push(data.id)
    selectCarsName.push(data.transSize)
    for (let item of transSizeData) {
      if (data.id === item.id) {
        item.isSelected = true
      }
    }
    dispatch({
      type: 'publishDriverLine/changeStates',
      payload: {
        transSizeData,
        selectCarsId,
        selectCarsName,
      },
    })
  }
  const submit = () => {
    dispatch({
      type: 'publishDriverLine/changeStates',
      payload: {
        visible: false,
      },
    })
  }
  const transSizeArray = transSizeData.map(item => {
    return (
      <li onClick={selectCars.bind(this, item)} className={item.isSelected && 'selected'}>
        <span>{item.transSize}</span>
        <i></i>
      </li>
    )
  })
  return (
    <Modal
      title="车长选择 (支持多选)"
      visible={visible}
      onCancel={hideModal}
      onOk={submit}
    >
      <ul className="my-check-box">
        {
          transSizeArray
        }
      </ul>
    </Modal>
  )
}
CarLengthModal.propTypes = {
  visible: PropTypes.string,
  transSizeData: PropTypes.array,
  dispatch: PropTypes.func,
  hideModal: PropTypes.func,
  selectCarsId: PropTypes.array,
  selectCarsName: PropTypes.array,
}
export default CarLengthModal
