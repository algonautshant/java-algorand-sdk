package algod

import (
	"context"
	"fmt"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// WaitForBlock /v2/status/wait-for-block-after/{round}
// Waits for a block to appear after round {round} and returns the node's status at
// the time.
type WaitForBlock struct {
	c     *Client
	round uint64
}

// Do performs HTTP request
func (s *WaitForBlock) Do(ctx context.Context,
	headers ...*common.Header) (response models.NodeStatusResponse, err error) {
	err = s.c.get(ctx, &response,
		fmt.Sprintf("/v2/status/wait-for-block-after/%d", s.round), nil, headers)
	return
}
