
// 时间转换为相对当前时间
function getDateDiff(dateTimeStamp){
  let result = '';
	let minute = 1000 * 60;
	let hour = minute * 60;
	let day = hour * 24;
	// let halfamonth = day * 15;
  let month = day * 30;
  let around = '前';
  let nowAround = '刚刚';
	let now = new Date().getTime();
	let diffValue = now - Date.parse(dateTimeStamp);
	if(diffValue < 0){
    around = '后';
    nowAround = '即将开始';
  };
  diffValue = Math.abs(diffValue);
	let monthC:number = diffValue/month;
	let weekC = diffValue/(7*day);
	let dayC = diffValue/day;
	let hourC = diffValue/hour;
	let minC = diffValue/minute;
	if(monthC>=1){
		result= `${parseInt(`${monthC}`, 10)}个月${around}`;
	}
	else if(weekC>=1){
		result= `${parseInt(`${weekC}`, 10)}周${around}`;
	}
	else if(dayC>=1){
		result= `${parseInt(`${dayC}`, 10)}天${around}`;
	}
	else if(hourC>=1){
		result= `${parseInt(`${hourC}`, 10)}小时${around}`;
	}
	else if(minC>=1){
		result= `${parseInt(`${minC}`, 10)}分钟${around}`;
	}else
	result= nowAround;
	return result;
}

export {getDateDiff}
