import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import QueryLine from './queryLine'
import LineList from './lineList'

const SpecialLine = ({
  specialLine,
  dispatch,
}) => {
  const { fields,
     tableTime,
     provinceFromData,
     cityFromData,
     countyFromData,
     provinceToData,
     cityToData,
     countyToData,
     modalState,
     selectData,
     collectionData,
     payData,
     teakUserData,
     recruitData,
     logisticsModal,
     collapse,
     } = specialLine
  const queryLineProps = {
    provinceFromData,
    cityFromData,
    countyFromData,
    provinceToData,
    cityToData,
    countyToData,
    selectData,
    collapse,
  }
  return (
    <div>
      <QueryLine queryLineProps={queryLineProps} dispatch={dispatch} />
      <LineList fields={fields}
        tableTime={tableTime}
        dispatch={dispatch}
        modalState={modalState}
        collectionData={collectionData}
        payData={payData}
        teakUserData={teakUserData}
        recruitData={recruitData}
        logisticsModal={logisticsModal}
      />
    </div>
  )
}

SpecialLine.propTypes = {
  specialLine: PropTypes.obj,
  dispatch: PropTypes.func.isRequired,
  modalState: PropTypes.obj,
  selectData: PropTypes.obj,
  collectionData: PropTypes.obj,
  recruitData: PropTypes.array,
  logisticsModal: PropTypes.obj,
}
export default connect(({ specialLine }) => ({ specialLine }))(SpecialLine)
