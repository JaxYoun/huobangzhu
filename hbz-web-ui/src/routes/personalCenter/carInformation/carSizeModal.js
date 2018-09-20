import React from 'react'
import { Modal } from 'antd'
import PropTypes from 'prop-types'

const CarSizeModal = ({
  visible,
  transSizeData,
  dispatch,
  hideCarSizeModal,
  selectCar,
}) => {
  const removeCar = (arr, val) => {
    return arr.map((item, index) => {
      if (item === val) {
        arr.splice(index, 1)
      }
      return arr
    })
  }
  const selectCars = (data) => {
    for (let item of transSizeData) {
      if (data.id === item.id) {
        if (item.isSelected) {
          item.isSelected = false
          removeCar(selectCar, item.transSize)
        } else {
          item.isSelected = true
          selectCar.push(item.transSize)
        }
      }
    }
    dispatch({
      type: 'carInformation/changeStates',
      payload: {
        transSizeData,
        selectCar,
      },
    })
  }
  const submit = () => {
    dispatch({
      type: 'carInformation/updateModal',
      payload: {
        visible: false,
      },
    })
  }
  const transSizeArray = transSizeData.map(item => {
    if (selectCar.length > 0) {
      for (let itemSize of selectCar) {
        if (item.transSize === itemSize) {
          item.isSelected = true
        }
      }
    }
    return (
      <li onClick={selectCars.bind(this, item)} className={item.isSelected ? 'selected' : ''}>
        <span>{item.transSize}</span>
        <i></i>
      </li>
    )
  })
  return (
    <Modal
      title="车长选择 (支持多选)"
      visible={visible}
      onCancel={hideCarSizeModal}
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
CarSizeModal.propTypes = {
  visible: PropTypes.string,
  transSizeData: PropTypes.array,
  dispatch: PropTypes.func,
  hideCarSizeModal: PropTypes.func,
  selectCar: PropTypes.array,
}
export default CarSizeModal
